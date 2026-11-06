import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Week 9 Assignment 4 - City Cab Fare Meter
 * Minimum fare (100) is shared in the abstract Cab class; night service is an optional ability (interface).
 */
public class A4_CityCabFareMeter {

    static final String SAMPLE = "4\nMINI 8 DAY\nSEDAN 10 NIGHT\nSUV 20 DAY\nMINI 5 NIGHT";

    interface NightService {
        double NIGHT_SURCHARGE = 0.20;
        default double applyNightSurcharge(double fare) { return fare * (1 + NIGHT_SURCHARGE); }
    }

    static abstract class Cab {
        static final double MINIMUM_FARE = 100;
        abstract String getType();
        abstract double ratePerKm();
        final double dayFare(double km) { return Math.max(km * ratePerKm(), MINIMUM_FARE); }
    }

    static class MiniCab extends Cab {
        String getType() { return "MINI"; }
        double ratePerKm() { return 10; }
    }

    static class SedanCab extends Cab implements NightService {
        String getType() { return "SEDAN"; }
        double ratePerKm() { return 14; }
    }

    static class SuvCab extends Cab implements NightService {
        String getType() { return "SUV"; }
        double ratePerKm() { return 18; }
    }

    static Cab create(String type) {
        switch (type.toUpperCase()) {
            case "MINI": return new MiniCab();
            case "SEDAN": return new SedanCab();
            case "SUV": return new SuvCab();
            default: throw new IllegalArgumentException("Unknown cab type: " + type);
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        int n = Integer.parseInt(lines.get(0).trim());
        double total = 0;
        for (int i = 1; i <= n; i++) {
            String[] p = lines.get(i).trim().split("\\s+");
            Cab cab = create(p[0]);
            double km = Double.parseDouble(p[1]);
            boolean night = p[2].equalsIgnoreCase("NIGHT");

            double fare = cab.dayFare(km);   // minimum applied first
            if (night) {
                if (!(cab instanceof NightService)) {
                    System.out.println(cab.getType() + ": night service not available");
                    continue;
                }
                fare = ((NightService) cab).applyNightSurcharge(fare);
            }
            total += fare;
            System.out.printf("%s: %.2f%n", cab.getType(), fare);
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
