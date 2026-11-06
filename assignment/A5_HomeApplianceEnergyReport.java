import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Week 9 Assignment 5 - Home Appliance Energy Report
 * Every appliance computes units the same way; only some support saver mode (SaverMode interface).
 */
public class A5_HomeApplianceEnergyReport {

    static final String SAMPLE = "4\nFRIDGE 24\nAC 8 SAVER\nTV 5\nWASHER 2 SAVER";
    static final double COST_PER_UNIT = 8;

    interface SaverMode {
        double SAVER_REDUCTION = 0.25;
        default double applySaver(double units) { return units * (1 - SAVER_REDUCTION); }
    }

    static abstract class Appliance {
        abstract String getName();
        abstract double powerWatts();
        final double units(double hours) { return powerWatts() * hours / 1000; } // kWh
    }

    static class Fridge extends Appliance {
        String getName() { return "FRIDGE"; }
        double powerWatts() { return 150; }
    }

    static class AirConditioner extends Appliance implements SaverMode {
        String getName() { return "AC"; }
        double powerWatts() { return 1500; }
    }

    static class Tv extends Appliance {
        String getName() { return "TV"; }
        double powerWatts() { return 100; }
    }

    static class WashingMachine extends Appliance implements SaverMode {
        String getName() { return "WASHER"; }
        double powerWatts() { return 500; }
    }

    static Appliance create(String name) {
        switch (name.toUpperCase()) {
            case "FRIDGE": return new Fridge();
            case "AC": return new AirConditioner();
            case "TV": return new Tv();
            case "WASHER": return new WashingMachine();
            default: throw new IllegalArgumentException("Unknown appliance: " + name);
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        int n = Integer.parseInt(lines.get(0).trim());
        double totalCost = 0;
        for (int i = 1; i <= n; i++) {
            String[] p = lines.get(i).trim().split("\\s+");
            Appliance appliance = create(p[0]);
            double units = appliance.units(Double.parseDouble(p[1]));
            boolean saver = p.length > 2 && p[2].equalsIgnoreCase("SAVER");

            if (saver) {
                if (!(appliance instanceof SaverMode)) {
                    System.out.println(appliance.getName() + ": saver mode not supported");
                    continue;
                }
                units = ((SaverMode) appliance).applySaver(units);
            }
            double cost = units * COST_PER_UNIT;
            totalCost += cost;
            System.out.printf("%s: Units=%.2f Cost=%.2f%n", appliance.getName(), units, cost);
        }
        System.out.printf("Total Cost: %.2f%n", totalCost);
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
