package ridesharingservice;

import ridesharingservice.entities.*;
import ridesharingservice.enums.DriverStatus;
import ridesharingservice.enums.RideType;
import ridesharingservice.strategy.matching.DriverMatchingStrategy;
import ridesharingservice.strategy.pricing.PricingStrategy;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

public class RideSharingService {
    private final Map<String, Rider> riders = new ConcurrentHashMap<>();
    private final Map<String, Driver> drivers = new ConcurrentHashMap<>();
    private final Map<String, Trip> trips = new ConcurrentHashMap<>();
    private final PricingStrategy pricingStrategy;
    private final DriverMatchingStrategy driverMatchingStrategy;

    public RideSharingService(PricingStrategy pricingStrategy, DriverMatchingStrategy driverMatchingStrategy) {
        if (pricingStrategy == null || driverMatchingStrategy == null) {
            throw new IllegalArgumentException("Strategies cannot be null.");
        }

        this.pricingStrategy = pricingStrategy;
        this.driverMatchingStrategy = driverMatchingStrategy;
    }

    public Rider registerRider(String name, String contact) {
        Rider rider = new Rider(name, contact);
        riders.put(rider.getId(), rider);
        return rider;
    }

    public Driver registerDriver(String name, String contact, Vehicle vehicle, Location initialLocation) {
        Driver driver = new Driver(name, contact, vehicle, initialLocation);
        drivers.put(driver.getId(), driver);
        return driver;
    }

    public Trip requestRide(String riderId, Location pickup, Location dropoff, RideType rideType) {
        Rider rider = riders.get(riderId);
        if (rider == null) {
            throw new NoSuchElementException("Rider not found");
        }
        System.out.println("\n--- New Ride Request from " + rider.getName() + " ---");
        List<Driver> availableDrivers = driverMatchingStrategy.findDrivers(List.copyOf(drivers.values()), pickup, rideType);
        if (availableDrivers.isEmpty()) {
            System.out.println("No drivers available for your request. " + "Please try again later.");
            return null;
        }
        System.out.println("Found " + availableDrivers.size() + " available driver(s).");
        double fare = pricingStrategy.calculateFare(pickup, dropoff, rideType);
        System.out.printf("Estimated fare: $%.2f%n", fare);
        Trip trip = new Trip(rider, pickup, dropoff, fare);
        trips.put(trip.getId(), trip);
        System.out.println("Notifying nearby drivers of the new ride request...");
        for (Driver driver : availableDrivers) {
            System.out.println(" > Notifying " + driver.getName() + " at " + driver.getCurrentLocation());
        }
        return trip;
    }

    public void acceptRide(String driverId, String tripId) {
        Driver driver = drivers.get(driverId);
        Trip trip = trips.get(tripId);
        if (driver == null || trip == null) {
            throw new NoSuchElementException("Driver or Trip not found");
        }
        System.out.println("\n--- Driver " + driver.getName() + " accepted the ride ---");
        if (!driver.tryAssignTrip()) {
            throw new IllegalStateException("Driver is not available");
        }
        trip.assignDriver(driver);
    }

    public void startTrip(String tripId) {
        Trip trip = trips.get(tripId);
        if (trip == null) {
            throw new NoSuchElementException("Trip not found");
        }
        System.out.println("\n--- Trip " + trip.getId() + " is starting ---");
        trip.startTrip();
    }

    public void endTrip(String tripId) {
        Trip trip = trips.get(tripId);
        if (trip == null) {
            throw new NoSuchElementException("Trip not found");
        }
        System.out.println("\n--- Trip " + trip.getId() + " is ending ---");
        trip.endTrip();
        Driver driver = trip.getDriver();
        driver.setStatus(DriverStatus.ONLINE);
        driver.setCurrentLocation(trip.getDropoffLocation());
        driver.addTripToHistory(trip);

        Rider rider = trip.getRider();
        rider.addTripToHistory(trip);
        System.out.println("Driver " + driver.getName() + " is now back online at " + driver.getCurrentLocation());
    }
}