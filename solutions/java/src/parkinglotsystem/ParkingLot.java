package parkinglotsystem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ParkingLot {
    private final List<ParkingSpot> spots;
    private final Map<String, Ticket> activeTickets;
    private final Set<String> occupiedSpotIds;
    private final long hourlyRateCents;

    public ParkingLot(List<ParkingSpot> spots, long hourlyRateCents) {
        this.spots = spots;
        this.activeTickets = new HashMap<>();
        this.occupiedSpotIds = new HashSet<>();
        this.hourlyRateCents = hourlyRateCents;
    }

    public Ticket enter(VehicleType vehicleType) {
        ParkingSpot spot = findAvailableSpot(vehicleType);
        if (spot == null) {
            throw new RuntimeException("No available spots for vehicle type " + vehicleType);
        }

        occupiedSpotIds.add(spot.getId());

        String ticketId = UUID.randomUUID().toString();
        long entryTime = System.currentTimeMillis();
        Ticket ticket = new Ticket(ticketId, spot.getId(), vehicleType, entryTime);

        activeTickets.put(ticketId, ticket);

        return ticket;
    }

    public long exit(String ticketId) {
        if (ticketId == null || ticketId.isEmpty()) {
            throw new RuntimeException("Invalid ticket ID");
        }

        Ticket ticket = activeTickets.get(ticketId);
        if (ticket == null) {
            throw new RuntimeException("Ticket not found or already used");
        }

        long exitTime = System.currentTimeMillis();
        long fee = computeFee(ticket.getEntryTime(), exitTime);

        occupiedSpotIds.remove(ticket.getSpotId());
        activeTickets.remove(ticketId);

        return fee;
    }

    private ParkingSpot findAvailableSpot(VehicleType vehicleType) {
        SpotType requiredSpotType = mapVehicleTypeToSpotType(vehicleType);

        for (ParkingSpot spot : spots) {
            if (!occupiedSpotIds.contains(spot.getId()) && spot.getSpotType() == requiredSpotType) {
                return spot;
            }
        }

        return null;
    }

    private SpotType mapVehicleTypeToSpotType(VehicleType vehicleType) {
        if (vehicleType == VehicleType.MOTORCYCLE) return SpotType.MOTORCYCLE;
        if (vehicleType == VehicleType.CAR) return SpotType.CAR;
        if (vehicleType == VehicleType.LARGE) return SpotType.LARGE;
        throw new RuntimeException("Unknown vehicle type");
    }

    private long computeFee(long entryTime, long exitTime) {
        long durationMillis = exitTime - entryTime;
        long durationHours = durationMillis / (1000 * 60 * 60);

        if (durationMillis % (1000 * 60 * 60) > 0) {
            durationHours++;
        }

        return durationHours * hourlyRateCents;
    }
}

