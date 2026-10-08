package hospital.models;

import hospital.interfaces.Reportable;

import java.util.ArrayList;
import java.util.List;

public class Patient extends User implements Reportable {
    private static final long serialVersionUID = 1L;

    private int age;
    private String homeDepartment;
    private String medicalHistorySummary;
    private boolean insuranceActive;
    private boolean discharged;

    private MedicalRecord medicalRecord;
    private List<Appointment> appointments;
    private List<BillingRecord> billingRecords;
    private List<String> activeMedications;

    public Patient(String id, String name, int age, String homeDepartment,
                   String medicalHistorySummary, boolean insuranceActive) {
        super(id, name, "patient@hospital.com", Role.PATIENT);

        this.age = age;
        this.homeDepartment = homeDepartment;
        this.medicalHistorySummary = medicalHistorySummary;
        this.insuranceActive = insuranceActive;
        this.discharged = false;

        this.medicalRecord = new MedicalRecord(id + "-MR", this);
        this.appointments = new ArrayList<>();
        this.billingRecords = new ArrayList<>();
        this.activeMedications = new ArrayList<>();
    }

    @Override
    public String getDashboardMessage() {
        return "Patient dashboard for " + getName();
    }

    @Override
    public String generateReport() {
        return "Patient ID: " + getId()
                + "\nName: " + getName()
                + "\nAge: " + age
                + "\nDepartment: " + homeDepartment
                + "\nMedical History: " + medicalHistorySummary
                + "\nInsurance Active: " + insuranceActive
                + "\nDischarged: " + discharged;
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public void addBillingRecord(BillingRecord bill) {
        billingRecords.add(bill);
    }

    public void addActiveMedication(String medicine) {
        activeMedications.add(medicine);
    }

    public int getAge() {
        return age;
    }

    public String getHomeDepartment() {
        return homeDepartment;
    }

    public String getMedicalHistorySummary() {
        return medicalHistorySummary;
    }

    public boolean isInsuranceActive() {
        return insuranceActive;
    }

    public void setInsuranceActive(boolean insuranceActive) {
        this.insuranceActive = insuranceActive;
    }

    public boolean isDischarged() {
        return discharged;
    }

    public void setDischarged(boolean discharged) {
        this.discharged = discharged;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public List<BillingRecord> getBillingRecords() {
        return billingRecords;
    }

    public List<String> getActiveMedications() {
        return activeMedications;
    }
}