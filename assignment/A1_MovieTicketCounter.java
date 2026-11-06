import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Week 9 Assignment 1 - Movie Ticket Counter
 * The convenience fee is written once, in the abstract Ticket class. A plain Ticket can't be created.
 */
public class A1_MovieTicketCounter {

    static final String SAMPLE = "3\nREGULAR 3\nPREMIUM 2\nRECLINER 1";

    static abstract class Ticket {
        static final double CONVENIENCE_FEE = 20; // the only place the fee exists
        protected final int count;
        Ticket(int count) { this.count = count; }
        abstract String getSeatType();
        abstract double pricePerTicket();
        final double amount() { return count * (pricePerTicket() + CONVENIENCE_FEE); }
    }

    static class RegularTicket extends Ticket {
        RegularTicket(int c) { super(c); }
        String getSeatType() { return "REGULAR"; }
        double pricePerTicket() { return 150; }
    }

    static class PremiumTicket extends Ticket {
        PremiumTicket(int c) { super(c); }
        String getSeatType() { return "PREMIUM"; }
        double pricePerTicket() { return 250; }
    }

    static class ReclinerTicket extends Ticket {
        ReclinerTicket(int c) { super(c); }
        String getSeatType() { return "RECLINER"; }
        double pricePerTicket() { return 400; }
    }

    static Ticket create(String seat, int count) {
        switch (seat.toUpperCase()) {
            case "REGULAR": return new RegularTicket(count);
            case "PREMIUM": return new PremiumTicket(count);
            case "RECLINER": return new ReclinerTicket(count);
            default: throw new IllegalArgumentException("Unknown seat type: " + seat);
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        int n = Integer.parseInt(lines.get(0).trim());
        List<Ticket> bookings = new ArrayList<Ticket>();
        for (int i = 1; i <= n; i++) {
            String[] p = lines.get(i).trim().split("\\s+");
            bookings.add(create(p[0], Integer.parseInt(p[1])));
        }
        double total = 0;
        for (Ticket t : bookings) {
            double amount = t.amount();
            total += amount;
            System.out.printf("%s: %.2f%n", t.getSeatType(), amount);
        }
        System.out.printf("Total: %.2f%n", total);
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
