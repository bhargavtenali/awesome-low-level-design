package ridesharingservice.entities;

import ridesharingservice.enums.DriverStatus;

public class Driver extends User {

    private final Vehicle vehicle;
    private Location currentLocation;
    private DriverStatus status;

    public Driver(
            String name,
            String contact,
            Vehicle vehicle,
            Location initialLocation) {

        super(name, contact);

        this.vehicle = vehicle;
        this.currentLocation = initialLocation;
        this.status = DriverStatus.OFFLINE;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public synchronized boolean tryAssignTrip() {

        if (status != DriverStatus.ONLINE) {
            return false;
        }

        status = DriverStatus.IN_TRIP;

        System.out.println(
                "Driver " + getName() + " is now " + status);

        return true;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;

        System.out.println(
                "Driver " + getName() + " is now " + status);
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}