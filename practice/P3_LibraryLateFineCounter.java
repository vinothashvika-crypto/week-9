import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Week 9 Practice 3 - Library Late Fine Counter (abstract fine calculation)
 * Book 2/day, DVD 5/day capped at 50, Magazine 1/day.
 */
public class P3_LibraryLateFineCounter {

    static final String SAMPLE = "3\nBOOK Algebra 4\nDVD Inception 12\nMAGAZINE Sports 3";

    static abstract class LibraryItem {
        protected final String title;
        protected final int daysLate;
        LibraryItem(String title, int daysLate) { this.title = title; this.daysLate = daysLate; }
        String getTitle() { return title; }
        abstract double calculateFine();
    }

    static class Book extends LibraryItem {
        Book(String t, int d) { super(t, d); }
        double calculateFine() { return 2.0 * daysLate; }
    }

    static class Dvd extends LibraryItem {
        Dvd(String t, int d) { super(t, d); }
        double calculateFine() { return Math.min(5.0 * daysLate, 50); }
    }

    static class Magazine extends LibraryItem {
        Magazine(String t, int d) { super(t, d); }
        double calculateFine() { return 1.0 * daysLate; }
    }

    static LibraryItem create(String[] p) {
        int days = Integer.parseInt(p[2]);
        switch (p[0].toUpperCase()) {
            case "BOOK": return new Book(p[1], days);
            case "DVD": return new Dvd(p[1], days);
            case "MAGAZINE": return new Magazine(p[1], days);
            default: throw new IllegalArgumentException("Unknown item type: " + p[0]);
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        int n = Integer.parseInt(lines.get(0).trim());
        List<LibraryItem> items = new ArrayList<LibraryItem>();
        for (int i = 1; i <= n; i++) items.add(create(lines.get(i).trim().split("\\s+")));

        double total = 0;
        for (LibraryItem item : items) {
            double fine = item.calculateFine();
            total += fine;
            System.out.printf("%s: %.2f%n", item.getTitle(), fine);
        }
        System.out.printf("Total Fines: %.2f%n", total);
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
