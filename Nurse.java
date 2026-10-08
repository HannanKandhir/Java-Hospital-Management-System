package hospital.models;

public class Nurse extends Staff {
    private static final long serialVersionUID = 1L;

    public Nurse(String id, String name, String email, String departmentName) {
        super(id, name, email, Role.NURSE, departmentName);
    }

    @Override
    public void performDuty() {
        System.out.println(getName() + " is assisting patients and doctors.");
    }

    public void recordVitals(Patient patient, String vitals) {
        patient.getMedicalRecord().addClinicalNote("Vitals recorded by " + getName() + ": " + vitals);
    }
}