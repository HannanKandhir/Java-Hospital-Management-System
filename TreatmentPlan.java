package hospital.models;

import hospital.interfaces.Reportable;

import java.io.Serializable;

public class TreatmentPlan implements Serializable, Reportable {
    private static final long serialVersionUID = 1L;

    private String id;
    private Patient patient;
    private Doctor responsibleDoctor;
    private String description;
    private String followUpInstructions;
    private boolean active;

    public TreatmentPlan(String id, Patient patient, Doctor responsibleDoctor,
                         String description, String followUpInstructions) {
        this.id = id;
        this.patient = patient;
        this.responsibleDoctor = responsibleDoctor;
        this.description = description;
        this.followUpInstructions = followUpInstructions;
        this.active = true;
    }

    @Override
    public String generateReport() {
        return "TreatmentPlan ID: " + id
                + ", Doctor: " + responsibleDoctor.getName()
                + ", Description: " + description
                + ", Follow-up: " + followUpInstructions
                + ", Active: " + active;
    }

    public void closePlan() {
        this.active = false;
    }

    public boolean isActive() {
        return active;
    }
}