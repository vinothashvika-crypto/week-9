import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Week 9 Practice 4 - Electricity Connection Billing (abstract class with shared units field)
 * Home: 5/unit for the first 100, then 7/unit. Shop: 8/unit + 100. Factory: 6/unit, minimum 1000.
 */
public class P4_ElectricityConnectionBilling {

    static final String SAMPLE = "3\nHOME 150\nSHOP 90\nFACTORY 120";

    static abstract class Connection {
        protected final double units;
        Connection(double units) { this.units = units; }
        abstract String getType();
        abstract double calculateBill();
    }

    static class HomeConnection extends Connection {
        HomeConnection(double u) { super(u); }
        String getType() { return "HOME"; }
        double calculateBill() {
            if (units <= 100) return units * 5;
            return 100 * 5 + (units - 100) * 7;
        }
    }

    static class ShopConnection extends Connection {
        ShopConnection(double u) { super(u); }
        String getType() { return "SHOP"; }
        double calculateBill() { return units * 8 + 100; }
    }

    static class FactoryConnection extends Connection {
        FactoryConnection(double u) { super(u); }
        String getType() { return "FACTORY"; }
        double calculateBill() { return Math.max(units * 6, 1000); }
    }

    static Connection create(String type, double units) {
        switch (type.toUpperCase()) {
            case "HOME": return new HomeConnection(units);
            case "SHOP": return new ShopConnection(units);
            case "FACTORY": return new FactoryConnection(units);
            default: throw new IllegalArgumentException("Unknown connection type: " + type);
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        int n = Integer.parseInt(lines.get(0).trim());
        List<Connection> connections = new ArrayList<Connection>();
        for (int i = 1; i <= n; i++) {
            String[] p = lines.get(i).trim().split("\\s+");
            connections.add(create(p[0], Double.parseDouble(p[1])));
        }
        double total = 0;
        for (Connection c : connections) {
            double bill = c.calculateBill();
            total += bill;
            System.out.printf("%s: %.2f%n", c.getType(), bill);
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
