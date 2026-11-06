import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Week 9 Assignment 3 - College Fee Counter
 * The transport fee lives only in the UsesCollegeBus interface.
 */
public class A3_CollegeFeeCounter {

    static final String SAMPLE = "3\nDAY_SCHOLAR Asha\nHOSTELLER Ravi\nSCHOLAR Neha";
    static final double NORMAL_TUITION = 40000;

    interface UsesCollegeBus {
        double TRANSPORT_FEE = 12000; // the only place this fee is written
        default double transportFee() { return TRANSPORT_FEE; }
    }

    static abstract class Student {
        protected final String name;
        Student(String name) { this.name = name; }
        String getName() { return name; }
        abstract double tuition();
        double extraCharges() { return 0; }   // hostellers override this

        double totalFee() {
            double fee = tuition() + extraCharges();
            if (this instanceof UsesCollegeBus) {   // a capability check, not a type-name check
                fee += ((UsesCollegeBus) this).transportFee();
            }
            return fee;
        }
    }

    static class DayScholar extends Student implements UsesCollegeBus {
        DayScholar(String n) { super(n); }
        double tuition() { return NORMAL_TUITION; }
    }

    static class Hosteller extends Student {
        static final double HOSTEL_FEE = 60000;
        Hosteller(String n) { super(n); }
        double tuition() { return NORMAL_TUITION; }
        double extraCharges() { return HOSTEL_FEE; }
    }

    static class ScholarshipStudent extends Student implements UsesCollegeBus {
        ScholarshipStudent(String n) { super(n); }
        double tuition() { return NORMAL_TUITION / 2; }
    }

    static Student create(String type, String name) {
        switch (type.toUpperCase()) {
            case "DAY_SCHOLAR": return new DayScholar(name);
            case "HOSTELLER": return new Hosteller(name);
            case "SCHOLAR": return new ScholarshipStudent(name);
            default: throw new IllegalArgumentException("Unknown student type: " + type);
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        int n = Integer.parseInt(lines.get(0).trim());
        List<Student> students = new ArrayList<Student>();
        for (int i = 1; i <= n; i++) {
            String[] p = lines.get(i).trim().split("\\s+");
            students.add(create(p[0], p[1]));
        }
        double total = 0;
        for (Student s : students) {
            double fee = s.totalFee();
            total += fee;
            System.out.printf("%s: %.2f%n", s.getName(), fee);
        }
        System.out.printf("Total Collected: %.2f%n", total);
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
