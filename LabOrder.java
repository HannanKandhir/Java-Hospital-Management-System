package hospital.models;

import hospital.interfaces.Reportable;

import java.io.Serializable;

public class LabOrder implements Serializable, Reportable {
    private static final long serialVersionUID = 1L;

    private String id;
    private Patient patient;
    private Doctor doctor;
    private String testName;
    private String priority;
    private boolean completed;
    private DiagnosticResult result;

    public LabOrder(String id, Patient patient, Doctor doctor,
                    String testName, String priority) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.testName = testName;
        this.priority = priority;
        this.completed = false;
    }

    @Override
    public String generateReport() {
        return "LabOrder ID: " + id
                + ", Test: " + testName
                + ", Priority: " + priority
                + ", Doctor: " + doctor.getName()
                + ", Completed: " + completed;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setResult(DiagnosticResult result) {
        this.result = result;
    }

    public DiagnosticResult getResult() {
        return result;
    }
}