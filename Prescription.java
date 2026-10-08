package hospital.models;

import hospital.interfaces.Reportable;

import java.io.Serializable;

public class Prescription implements Serializable, Reportable {
    private static final long serialVersionUID = 1L;

    private String id;
    private Patient patient;
    private Doctor doctor;
    private String medicineName;
    private String dosage;
    private boolean dispensed;
    private String authorizedBy;

    public Prescription(String id, Patient patient, Doctor doctor,
                        String medicineName, String dosage) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.dispensed = false;
        this.authorizedBy = "Not authorized yet";
    }

    @Override
    public String generateReport() {
        return "Prescription ID: " + id
                + ", Medicine: " + medicineName
                + ", Dosage: " + dosage
                + ", Doctor: " + doctor.getName()
                + ", Dispensed: " + dispensed
                + ", Authorized By: " + authorizedBy;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public boolean isDispensed() {
        return dispensed;
    }

    public void setDispensed(boolean dispensed) {
        this.dispensed = dispensed;
    }

    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }
}