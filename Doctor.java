package hospital.models;

import hospital.exceptions.DrugInteractionException;
import hospital.exceptions.UnauthorizedAccessException;
import hospital.interfaces.Schedulable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class Doctor extends Staff implements Schedulable {
    private static final long serialVersionUID = 1L;

    private String specialization;
    private List<LocalDateTime> unavailableSlots;

    public Doctor(String id, String name, String email, Role role,
                  String specialization, String departmentName) {
        super(id, name, email, role, departmentName);

        this.specialization = specialization;
        this.unavailableSlots = new ArrayList<>();
    }

    @Override
    public boolean isAvailable(LocalDateTime dateTime) {
        return !unavailableSlots.contains(dateTime);
    }

    public void addUnavailableSlot(LocalDateTime dateTime) {
        unavailableSlots.add(dateTime);
    }

    public void addDiagnosis(Patient patient, String diagnosis) throws UnauthorizedAccessException {
        if (!(getRole() == Role.GENERAL_PHYSICIAN || getRole() == Role.SPECIALIST)) {
            throw new UnauthorizedAccessException("Only doctors can add diagnosis.");
        }

        patient.getMedicalRecord().addDiagnosis(diagnosis, getName());
    }

    public TreatmentPlan createTreatmentPlan(String id, Patient patient,
                                             String description, String instructions)
            throws UnauthorizedAccessException {

        if (!(getRole() == Role.GENERAL_PHYSICIAN || getRole() == Role.SPECIALIST)) {
            throw new UnauthorizedAccessException("Only doctors can create treatment plans.");
        }

        TreatmentPlan plan = new TreatmentPlan(id, patient, this, description, instructions);
        patient.getMedicalRecord().addTreatmentPlan(plan);

        return plan;
    }

    public Prescription issuePrescription(String id, Patient patient,
                                          String medicineName, String dosage)
            throws UnauthorizedAccessException, DrugInteractionException {

        if (!(getRole() == Role.GENERAL_PHYSICIAN || getRole() == Role.SPECIALIST)) {
            throw new UnauthorizedAccessException("Only doctors can issue prescriptions.");
        }

        checkDrugInteraction(patient, medicineName);

        Prescription prescription = new Prescription(id, patient, this, medicineName, dosage);
        patient.getMedicalRecord().addPrescription(prescription);

        return prescription;
    }

    private void checkDrugInteraction(Patient patient, String medicineName)
            throws DrugInteractionException {

        for (String activeMedicine : patient.getActiveMedications()) {
            if (activeMedicine.equalsIgnoreCase("Metoprolol")
                    && medicineName.equalsIgnoreCase("Verapamil")) {
                throw new DrugInteractionException("Dangerous interaction: Metoprolol + Verapamil.");
            }
        }
    }

    public LabOrder createLabOrder(String id, Patient patient, String testName, String priority)
            throws UnauthorizedAccessException {

        if (!(getRole() == Role.GENERAL_PHYSICIAN || getRole() == Role.SPECIALIST)) {
            throw new UnauthorizedAccessException("Only doctors can create lab orders.");
        }

        LabOrder order = new LabOrder(id, patient, this, testName, priority);
        patient.getMedicalRecord().addLabOrder(order);

        return order;
    }

    public String getSpecialization() {
        return specialization;
    }
}