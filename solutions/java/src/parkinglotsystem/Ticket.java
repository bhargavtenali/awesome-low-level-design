package parkinglotsystem;

public class Ticket {
    private final String id;
    private final String spotId;
    private final VehicleType vehicleType;
    private final long entryTime;

    public Ticket(String id, String spotId, VehicleType vehicleType, long entryTime) {
        this.id = id;
        this.spotId = spotId;
        this.vehicleType = vehicleType;
        this.entryTime = entryTime;
    }

    public String getId() {
        return id;
    }

    public String getSpotId() {
        return spotId;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public long getEntryTime() {
        return entryTime;
    }
}

