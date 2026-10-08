package hospital.models;

import hospital.interfaces.Reportable;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class Department implements Serializable, Reportable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private int dailyAppointmentLimit;
    private int currentAppointments;
    private Queue<Patient> waitlist;

    public Department(String id, String name, int dailyAppointmentLimit) {
        this.id = id;
        this.name = name;
        this.dailyAppointmentLimit = dailyAppointmentLimit;
        this.currentAppointments = 0;
        this.waitlist = new LinkedList<>();
    }

    public boolean hasCapacity() {
        return currentAppointments < dailyAppointmentLimit;
    }

    public void increaseCurrentAppointments() {
        currentAppointments++;
    }

    public void addToWaitlist(Patient patient) {
        waitlist.add(patient);
    }

    @Override
    public String generateReport() {
        return "Department: " + name
                + ", Limit: " + dailyAppointmentLimit
                + ", Current Appointments: " + currentAppointments
                + ", Waitlist: " + waitlist.size();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Queue<Patient> getWaitlist() {
        return waitlist;
    }
}