import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Week 9 Practice 1 - Garden Plot Area Report (abstract class)
 * Adding a new shape only needs a new subclass; the report loop never changes.
 */
public class P1_GardenPlotAreaReport {

    static final String SAMPLE = "3\nCIRCLE Asha 5\nRECTANGLE Ravi 4 6\nTRIANGLE Neha 10 3";

    static abstract class Plot {
        protected final String owner;
        Plot(String owner) { this.owner = owner; }
        abstract String getShape();
        abstract double area();
        String getOwner() { return owner; }
    }

    static class CirclePlot extends Plot {
        private final double radius;
        CirclePlot(String owner, double radius) { super(owner); this.radius = radius; }
        String getShape() { return "CIRCLE"; }
        double area() { return Math.PI * radius * radius; }
    }

    static class RectanglePlot extends Plot {
        private final double length, width;
        RectanglePlot(String owner, double length, double width) { super(owner); this.length = length; this.width = width; }
        String getShape() { return "RECTANGLE"; }
        double area() { return length * width; }
    }

    static class TrianglePlot extends Plot {
        private final double base, height;
        TrianglePlot(String owner, double base, double height) { super(owner); this.base = base; this.height = height; }
        String getShape() { return "TRIANGLE"; }
        double area() { return 0.5 * base * height; }
    }

    static Plot create(String[] p) {
        switch (p[0].toUpperCase()) {
            case "CIRCLE": return new CirclePlot(p[1], Double.parseDouble(p[2]));
            case "RECTANGLE": return new RectanglePlot(p[1], Double.parseDouble(p[2]), Double.parseDouble(p[3]));
            case "TRIANGLE": return new TrianglePlot(p[1], Double.parseDouble(p[2]), Double.parseDouble(p[3]));
            default: throw new IllegalArgumentException("Unknown shape: " + p[0]);
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        int n = Integer.parseInt(lines.get(0).trim());
        List<Plot> plots = new ArrayList<Plot>();
        for (int i = 1; i <= n; i++) plots.add(create(lines.get(i).trim().split("\\s+")));

        double total = 0;
        for (Plot plot : plots) {
            double area = plot.area();
            total += area;
            System.out.printf("%s (%s): %.2f%n", plot.getOwner(), plot.getShape(), area);
        }
        System.out.printf("Total Area: %.2f%n", total);
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
