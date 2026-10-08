package hospital.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditTrail implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Entry> entries;

    public AuditTrail() {
        entries = new ArrayList<>();
    }

    public void addEntry(String action) {
        entries.add(new Entry(action));
    }

    public void printTrail() {
        for (Entry entry : entries) {
            System.out.println(entry);
        }
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public class Entry implements Serializable {
        private static final long serialVersionUID = 1L;

        private LocalDateTime time;
        private String action;
        private int hashCodeValue;

        public Entry(String action) {
            this.time = LocalDateTime.now();
            this.action = action;
            this.hashCodeValue = (time.toString() + action).hashCode();
        }

        @Override
        public String toString() {
            return time + " | " + action + " | Hash: " + hashCodeValue;
        }
    }
}