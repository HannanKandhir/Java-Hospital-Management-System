package hospital.models;

import hospital.interfaces.Reportable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecord implements Serializable, Reportable {
    private static final long serialVersionUID = 1L;

    private String recordId;
    private Patient patient;
    private List<String> diagnoses;
    private List<String> clinicalNotes;
    private List<TreatmentPlan> treatmentPlans;
    private List<Prescription> prescriptions;
    private List<LabOrder> labOrders;
    private List<DiagnosticResult> diagnosticResults;

    public MedicalRecord(String recordId, Patient patient) {
        this.recordId = recordId;
        this.patient = patient;

        this.diagnoses = new ArrayList<>();
        this.clinicalNotes = new ArrayList<>();
        this.treatmentPlans = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
        this.labOrders = new ArrayList<>();
        this.diagnosticResults = new ArrayList<>();
    }

    public void addDiagnosis(String diagnosis, String doctorName) {
        diagnoses.add("Doctor: " + doctorName + " | Diagnosis: " + diagnosis);
    }

    public void addClinicalNote(String note) {
        clinicalNotes.add(note);
    }

    public void addTreatmentPlan(TreatmentPlan plan) {
        treatmentPlans.add(plan);
    }

    public void addPrescription(Prescription prescription) {
        prescriptions.add(prescription);
    }

    public void addLabOrder(LabOrder order) {
        labOrders.add(order);
    }

    public void addDiagnosticResult(DiagnosticResult result) {
        diagnosticResults.add(result);
    }

    @Override
    public String generateReport() {
        StringBuilder sb = new StringBuilder();

        sb.append("Medical Record ID: ").append(recordId).append("\n");
        sb.append("Patient: ").append(patient.getName()).append("\n");

        sb.append("Diagnoses:\n");
        diagnoses.forEach(d -> sb.append("- ").append(d).append("\n"));

        sb.append("Clinical Notes:\n");
        clinicalNotes.forEach(n -> sb.append("- ").append(n).append("\n"));

        sb.append("Treatment Plans:\n");
        treatmentPlans.forEach(t -> sb.append("- ").append(t.generateReport()).append("\n"));

        sb.append("Prescriptions:\n");
        prescriptions.forEach(p -> sb.append("- ").append(p.generateReport()).append("\n"));

        sb.append("Lab Orders:\n");
        labOrders.forEach(l -> sb.append("- ").append(l.generateReport()).append("\n"));

        sb.append("Diagnostic Results:\n");
        diagnosticResults.forEach(r -> sb.append("- ").append(r.generateReport()).append("\n"));

        return sb.toString();
    }
}