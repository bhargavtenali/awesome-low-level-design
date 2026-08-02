package moviebookingsystem;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Showtime {
    private final String id;
    private final Theater theater;
    private final LocalDateTime datetime;
    private final String screenLabel;
    private final Movie movie;
    private final List<Reservation> reservations;

    public Showtime(String id, Theater theater, Movie movie, LocalDateTime datetime, String screenLabel) {
        this.id = id;
        this.theater = theater;
        this.movie = movie;
        this.datetime = datetime;
        this.screenLabel = screenLabel;
        this.reservations = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public Theater getTheater() {
        return theater;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public Movie getMovie() {
        return movie;
    }

    public boolean isAvailable(String seatId) {
        for (Reservation reservation : reservations) {
            if (reservation.getSeatIds().contains(seatId)) {
                return false;
            }
        }
        return true;
    }

    public List<String> getAvailableSeats() {
        Set<String> booked = new HashSet<>();
        for (Reservation reservation : reservations) {
            booked.addAll(reservation.getSeatIds());
        }

        List<String> available = new ArrayList<>();
        for (char row = 'A'; row <= 'Z'; row++) {
            for (int num = 0; num <= 20; num++) {
                String seatId = "" + row + num;
                if (!booked.contains(seatId)) {
                    available.add(seatId);
                }
            }
        }
        return available;
    }

    public void book(Reservation reservation) {
        synchronized (this) {
            List<String> seatIds = reservation.getSeatIds();

            if (seatIds == null || seatIds.isEmpty()) {
                throw new IllegalArgumentException("Must select at least one seat");
            }

            for (String seatId : seatIds) {
                if (!isValidSeatId(seatId)) {
                    throw new IllegalArgumentException("Invalid seat: " + seatId);
                }
            }

            for (String seatId : seatIds) {
                if (!isAvailable(seatId)) {
                    throw new IllegalStateException("Seat " + seatId + " is not available");
                }
            }

            reservations.add(reservation);
        }
    }

    public void cancel(Reservation reservation) {
        synchronized (this) {
            reservations.remove(reservation);
        }
    }

    private boolean isValidSeatId(String seatId) {
        if (seatId == null || seatId.length() < 2) {
            return false;
        }
        char row = seatId.charAt(0);
        try {
            int num = Integer.parseInt(seatId.substring(1));
            return row >= 'A' && row <= 'Z' && num >= 0 && num <= 20;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
