package hospital.models;

import hospital.interfaces.Reportable;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Appointment implements Serializable, Reportable {
    private static final long serialVersionUID = 1L;

    private String id;
    private Patient patient;
    private Doctor doctor;
    private Department department;
    private LocalDateTime dateTime;
    private AppointmentType type;
    private AppointmentStatus status;

    public Appointment(String id, Patient patient, Doctor doctor,
                       Department department, LocalDateTime dateTime,
                       AppointmentType type) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.department = department;
        this.dateTime = dateTime;
        this.type = type;
        this.status = AppointmentStatus.REQUESTED;
    }

    @Override
    public String generateReport() {
        return "Appointment ID: " + id
                + ", Patient: " + patient.getName()
                + ", Doctor: " + doctor.getName()
                + ", Department: " + department.getName()
                + ", Time: " + dateTime
                + ", Type: " + type
                + ", Status: " + status;
    }

    public String getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Department getDepartment() {
        return department;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public AppointmentType getType() {
        return type;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}