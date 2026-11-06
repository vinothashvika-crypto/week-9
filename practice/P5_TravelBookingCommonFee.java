import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Week 9 Practice 5 - Travel Booking with a Common Fee
 * The booking fee lives in ONE place (the abstract base class); subclasses only define the base fare.
 */
public class P5_TravelBookingCommonFee {

    static final String SAMPLE = "3\nBUS 200\nTRAIN 300\nFLIGHT 500";

    static abstract class Booking {
        static final double BOOKING_FEE = 50; // change the fee here and nowhere else
        protected final double distanceKm;
        Booking(double distanceKm) { this.distanceKm = distanceKm; }
        abstract String getMode();
        abstract double baseFare();
        final double totalFare() { return baseFare() + BOOKING_FEE; }
    }

    static class BusBooking extends Booking {
        BusBooking(double d) { super(d); }
        String getMode() { return "BUS"; }
        double baseFare() { return 2 * distanceKm; }
    }

    static class TrainBooking extends Booking {
        TrainBooking(double d) { super(d); }
        String getMode() { return "TRAIN"; }
        double baseFare() { return 1.5 * distanceKm; }
    }

    static class FlightBooking extends Booking {
        FlightBooking(double d) { super(d); }
        String getMode() { return "FLIGHT"; }
        double baseFare() { return 2500 + 4 * distanceKm; }
    }

    static Booking create(String mode, double km) {
        switch (mode.toUpperCase()) {
            case "BUS": return new BusBooking(km);
            case "TRAIN": return new TrainBooking(km);
            case "FLIGHT": return new FlightBooking(km);
            default: throw new IllegalArgumentException("Unknown mode: " + mode);
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        int n = Integer.parseInt(lines.get(0).trim());
        List<Booking> bookings = new ArrayList<Booking>();
        for (int i = 1; i <= n; i++) {
            String[] p = lines.get(i).trim().split("\\s+");
            bookings.add(create(p[0], Double.parseDouble(p[1])));
        }
        for (Booking b : bookings) {
            System.out.printf("%s: %.2f%n", b.getMode(), b.totalFare());
        }
    }

    // Reads piped/redirected input; falls back to the sample when nothing is provided
    static List<String> readInput() throws Exception {
        for (int i = 0; i < 10 && System.in.available() == 0; i++) Thread.sleep(20);
        List<String> lines = new ArrayList<String>();
        if (System.in.available() > 0) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            while ((line = reader.readLine()) != null) if (!line.trim().isEmpty()) lines.add(line);
        }
        if (lines.isEmpty()) {
            System.err.println("(no input given - using the sample input)");
            for (String line : SAMPLE.split("\n")) lines.add(line);
        }
        return lines;
    }
}
