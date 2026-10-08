package hospital.models;

import hospital.exceptions.UnauthorizedAccessException;

public class LabTechnician extends Staff {
    private static final long serialVersionUID = 1L;

    public LabTechnician(String id, String name, String email) {
        super(id, name, email, Role.LAB_TECHNICIAN, "Laboratory");
    }

    @Override
    public void performDuty() {
        System.out.println(getName() + " is processing lab tests.");
    }

    public DiagnosticResult uploadResult(String id, LabOrder order, String resultDetails)
            throws UnauthorizedAccessException {

        if (getRole() != Role.LAB_TECHNICIAN) {
            throw new UnauthorizedAccessException("Only lab technician can upload results.");
        }

        DiagnosticResult result = new DiagnosticResult(id, order, resultDetails, getName());

        order.setResult(result);
        order.setCompleted(true);

        order.getPatient().getMedicalRecord().addDiagnosticResult(result);
        order.getPatient().getMedicalRecord().addClinicalNote("Lab result uploaded by " + getName());

        return result;
    }
}