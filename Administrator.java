package hospital.models;

import hospital.exceptions.UnauthorizedAccessException;

public class Administrator extends Staff {
    private static final long serialVersionUID = 1L;

    public Administrator(String id, String name, String email) {
        super(id, name, email, Role.ADMINISTRATOR, "Administration");
    }

    @Override
    public void performDuty() {
        System.out.println(getName() + " is managing hospital administration.");
    }

    public void dischargePatient(Patient patient) throws UnauthorizedAccessException {
        if (getRole() != Role.ADMINISTRATOR) {
            throw new UnauthorizedAccessException("Only administrator can discharge patients.");
        }

        patient.setDischarged(true);
        patient.getMedicalRecord().addClinicalNote("Patient discharged by " + getName());
    }
}