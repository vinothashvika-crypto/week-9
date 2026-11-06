import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Week 9 Assignment 2 - Parcel Shipping Desk
 * Abstract class answers "how much does shipping cost?" (every parcel).
 * Insurable interface answers "can it be insured?" (only some parcels).
 */
public class A2_ParcelShippingDesk {

    static final String SAMPLE = "3\nSTANDARD 3 500\nEXPRESS 2 1000\nFRAGILE 4 2000";

    interface Insurable {
        double INSURANCE_RATE = 0.02; // 2% of declared value
        double getDeclaredValue();
        default double insurance() { return getDeclaredValue() * INSURANCE_RATE; }
    }

    static abstract class Parcel {
        protected final double weightKg;
        protected final double declaredValue;
        Parcel(double weightKg, double declaredValue) { this.weightKg = weightKg; this.declaredValue = declaredValue; }
        abstract String getType();
        abstract double shippingCharge();
        public double getDeclaredValue() { return declaredValue; }
    }

    static class StandardParcel extends Parcel {
        StandardParcel(double w, double v) { super(w, v); }
        String getType() { return "STANDARD"; }
        double shippingCharge() { return 40 + 10 * weightKg; }
    }

    static class ExpressParcel extends Parcel implements Insurable {
        ExpressParcel(double w, double v) { super(w, v); }
        String getType() { return "EXPRESS"; }
        double shippingCharge() { return 80 + 15 * weightKg; }
    }

    // Fragile = standard charge + 50 handling fee, and it can be insured
    static class FragileParcel extends StandardParcel implements Insurable {
        FragileParcel(double w, double v) { super(w, v); }
        String getType() { return "FRAGILE"; }
        double shippingCharge() { return super.shippingCharge() + 50; }
    }

    static Parcel create(String type, double weight, double value) {
        switch (type.toUpperCase()) {
            case "STANDARD": return new StandardParcel(weight, value);
            case "EXPRESS": return new ExpressParcel(weight, value);
            case "FRAGILE": return new FragileParcel(weight, value);
            default: throw new IllegalArgumentException("Unknown parcel type: " + type);
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        int n = Integer.parseInt(lines.get(0).trim());
        List<Parcel> parcels = new ArrayList<Parcel>();
        for (int i = 1; i <= n; i++) {
            String[] p = lines.get(i).trim().split("\\s+");
            parcels.add(create(p[0], Double.parseDouble(p[1]), Double.parseDouble(p[2])));
        }
        double grandTotal = 0;
        for (Parcel parcel : parcels) {
            double charge = parcel.shippingCharge();
            // Ask about the ability (Insurable), not the specific parcel type
            double insurance = (parcel instanceof Insurable) ? ((Insurable) parcel).insurance() : 0;
            double total = charge + insurance;
            grandTotal += total;
            System.out.printf("%s: Charge=%.2f Insurance=%.2f Total=%.2f%n", parcel.getType(), charge, insurance, total);
        }
        System.out.printf("Grand Total: %.2f%n", grandTotal);
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
