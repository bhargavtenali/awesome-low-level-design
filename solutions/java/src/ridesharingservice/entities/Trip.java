package ridesharingservice.entities;

import ridesharingservice.enums.TripStatus;
import ridesharingservice.state.RequestedState;
import ridesharingservice.state.TripState;

import java.util.UUID;

public class Trip {
    private final String id;
    private final Rider rider;
    private final Location pickupLocation;
    private final Location dropoffLocation;
    private final double fare;
    private Driver driver;
    private TripStatus status;
    private TripState currentState;

    public Trip(Rider rider, Location pickupLocation, Location dropoffLocation, double fare) {
        if (rider == null || pickupLocation == null || dropoffLocation == null) {
            throw new IllegalArgumentException("Rider, pickup and dropoff locations are required.");
        }
        this.id = UUID.randomUUID().toString();
        this.rider = rider;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.fare = fare;
        this.status = TripStatus.REQUESTED;
        this.currentState = new RequestedState();
    }

    public void assignDriver(Driver driver) {
        currentState.assign(this, driver);
    }

    public void startTrip() {
        currentState.start(this);
    }

    public void endTrip() {
        currentState.end(this);
    }

    public String getId() {
        return id;
    }

    public Rider getRider() {
        return rider;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Location getPickupLocation() {
        return pickupLocation;
    }

    public Location getDropoffLocation() {
        return dropoffLocation;
    }

    public double getFare() {
        return fare;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public void setState(TripState state) {
        this.currentState = state;
    }

    @Override
    public String toString() {
        return "Trip [id=" + id
                + ", status=" + status
                + ", fare=$" + String.format("%.2f", fare) + "]";
    }
}