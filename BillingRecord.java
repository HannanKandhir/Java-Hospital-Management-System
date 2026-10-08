package hospital.models;

import hospital.interfaces.Billable;
import hospital.interfaces.Reportable;

import java.io.Serializable;

public class BillingRecord implements Serializable, Billable, Reportable {
    private static final long serialVersionUID = 1L;

    private String id;
    private Patient patient;
    private double consultationFee;
    private double labFee;
    private double medicineFee;
    private double insuranceCoveragePercent;

    public BillingRecord(String id, Patient patient, double consultationFee,
                         double labFee, double medicineFee) {
        this.id = id;
        this.patient = patient;
        this.consultationFee = consultationFee;
        this.labFee = labFee;
        this.medicineFee = medicineFee;
        this.insuranceCoveragePercent = patient.isInsuranceActive() ? 70 : 0;
    }

    @Override
    public double calculateTotal() {
        return consultationFee + labFee + medicineFee;
    }

    public double getInsuranceCoveredAmount() {
        return calculateTotal() * insuranceCoveragePercent / 100;
    }

    public double getPatientDueAmount() {
        return calculateTotal() - getInsuranceCoveredAmount();
    }

    @Override
    public String generateReport() {
        return "Billing ID: " + id
                + ", Patient: " + patient.getName()
                + ", Total: PKR " + calculateTotal()
                + ", Insurance Covered: PKR " + getInsuranceCoveredAmount()
                + ", Patient Due: PKR " + getPatientDueAmount();
    }

    public double getTotalAmount() {
        return calculateTotal();
    }
}