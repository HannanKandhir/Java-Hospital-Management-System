package hospital.models;

import hospital.exceptions.DrugInteractionException;
import hospital.exceptions.UnauthorizedAccessException;

public class Pharmacist extends Staff {
    private static final long serialVersionUID = 1L;

    public Pharmacist(String id, String name, String email) {
        super(id, name, email, Role.PHARMACIST, "Pharmacy");
    }

    @Override
    public void performDuty() {
        System.out.println(getName() + " is validating and dispensing medicines.");
    }

    public void validateAndDispense(Patient patient, Prescription prescription)
            throws UnauthorizedAccessException, DrugInteractionException {

        if (getRole() != Role.PHARMACIST) {
            throw new UnauthorizedAccessException("Only pharmacist can dispense medicine.");
        }

        if (prescription.isDispensed()) {
            throw new DrugInteractionException("Prescription already dispensed.");
        }

        prescription.setDispensed(true);
        prescription.setAuthorizedBy(getName());

        patient.addActiveMedication(prescription.getMedicineName());

        patient.getMedicalRecord().addClinicalNote(
                "Medicine dispensed: " + prescription.getMedicineName() + " by " + getName()
        );
    }
}