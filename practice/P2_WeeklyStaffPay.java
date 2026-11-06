import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Week 9 Practice 2 - Weekly Staff Pay (abstract class: a plain "staff member" can never be created)
 * Hourly staff: rate for the first 40 hours, 1.5 x rate for each hour above 40.
 */
public class P2_WeeklyStaffPay {

    static final String SAMPLE = "3\nFULLTIME Asha 12000\nHOURLY Ravi 45 200\nINTERN Neha 5000";

    static abstract class StaffMember {
        protected final String name;
        StaffMember(String name) { this.name = name; }
        String getName() { return name; }
        abstract double weeklyPay();   // every subclass must define its pay rule
    }

    static class FullTimeStaff extends StaffMember {
        private final double weeklySalary;
        FullTimeStaff(String name, double weeklySalary) { super(name); this.weeklySalary = weeklySalary; }
        double weeklyPay() { return weeklySalary; }
    }

    static class HourlyStaff extends StaffMember {
        private final double hours, rate;
        HourlyStaff(String name, double hours, double rate) { super(name); this.hours = hours; this.rate = rate; }
        double weeklyPay() {
            double regular = Math.min(hours, 40);
            double overtime = Math.max(hours - 40, 0);
            return regular * rate + overtime * rate * 1.5;
        }
    }

    static class Intern extends StaffMember {
        private final double stipend;
        Intern(String name, double stipend) { super(name); this.stipend = stipend; }
        double weeklyPay() { return stipend; }
    }

    static StaffMember create(String[] p) {
        switch (p[0].toUpperCase()) {
            case "FULLTIME": return new FullTimeStaff(p[1], Double.parseDouble(p[2]));
            case "HOURLY": return new HourlyStaff(p[1], Double.parseDouble(p[2]), Double.parseDouble(p[3]));
            case "INTERN": return new Intern(p[1], Double.parseDouble(p[2]));
            default: throw new IllegalArgumentException("Unknown staff type: " + p[0]);
        }
    }

    public static void main(String[] args) throws Exception {
        List<String> lines = readInput();
        int n = Integer.parseInt(lines.get(0).trim());
        List<StaffMember> staff = new ArrayList<StaffMember>();
        for (int i = 1; i <= n; i++) staff.add(create(lines.get(i).trim().split("\\s+")));

        double total = 0;
        for (StaffMember s : staff) {
            double pay = s.weeklyPay();
            total += pay;
            System.out.printf("%s: %.2f%n", s.getName(), pay);
        }
        System.out.printf("Total Payroll: %.2f%n", total);
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
