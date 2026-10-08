package hospital.models;

import hospital.interfaces.Reportable;

import java.io.Serializable;

public class DiagnosticResult implements Serializable, Reportable {
    private static final long serialVersionUID = 1L;

    private String id;
    private LabOrder labOrder;
    private String resultDetails;
    private String uploadedBy;

    public DiagnosticResult(String id, LabOrder labOrder,
                            String resultDetails, String uploadedBy) {
        this.id = id;
        this.labOrder = labOrder;
        this.resultDetails = resultDetails;
        this.uploadedBy = uploadedBy;
    }

    @Override
    public String generateReport() {
        return "Diagnostic Result ID: " + id
                + ", Result: " + resultDetails
                + ", Uploaded By: " + uploadedBy;
    }
}