package hospital;

import hospital.exceptions.*;
import hospital.models.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HospitalSystem implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Patient> patients;
    private Map<String, Staff> staffMembers;
    private Map<String, Department> departments;
    private List<Appointment> appointments;
    private List<BillingRecord> bills;
    private AuditTrail auditTrail;

    public HospitalSystem() {
        patients = new HashMap<>();
        staffMembers = new HashMap<>();
        departments = new HashMap<>();
        appointments = new ArrayList<>();
        bills = new ArrayList<>();
        auditTrail = new AuditTrail();
    }

    public void addDepartment(Department department) throws DuplicateRecordException {
        if (departments.containsKey(department.getId())) {
            throw new DuplicateRecordException("Department already exists: " + department.getId());
        }

        departments.put(department.getId(), department);
        auditTrail.addEntry("Department added: " + department.getName());
    }

    public void addStaff(Staff staff) throws DuplicateRecordException {
        if (staffMembers.containsKey(staff.getId())) {
            throw new DuplicateRecordException("Staff member already exists: " + staff.getId());
        }

        staffMembers.put(staff.getId(), staff);
        auditTrail.addEntry("Staff added: " + staff.getName() + " role: " + staff.getRole());
    }

    public void registerPatient(Patient patient) throws DuplicateRecordException {
        if (patients.containsKey(patient.getId())) {
            throw new DuplicateRecordException("Patient already registered: " + patient.getId());
        }

        patients.put(patient.getId(), patient);
        auditTrail.addEntry("Patient registered: " + patient.getName());
    }

    public void bookAppointment(Appointment appointment)
            throws AppointmentConflictException, InvalidBillingException {

        Patient patient = appointment.getPatient();

        if (!patient.isInsuranceActive()) {
            throw new InvalidBillingException("Patient insurance/payment status is not active.");
        }

        for (Appointment existing : appointments) {
            boolean samePatient = existing.getPatient().getId().equals(patient.getId());
            boolean sameDoctor = existing.getDoctor().getId().equals(appointment.getDoctor().getId());
            boolean sameTime = existing.getDateTime().equals(appointment.getDateTime());

            if ((samePatient || sameDoctor) && sameTime) {
                throw new AppointmentConflictException("Appointment conflict detected at " + appointment.getDateTime());
            }
        }

        if (!appointment.getDepartment().hasCapacity()) {
            appointment.setStatus(AppointmentStatus.WAITLISTED);
            appointment.getDepartment().addToWaitlist(patient);
            auditTrail.addEntry("Appointment waitlisted for patient: " + patient.getName());
        } else {
            appointment.setStatus(AppointmentStatus.CONFIRMED);
            appointment.getDepartment().increaseCurrentAppointments();
            auditTrail.addEntry("Appointment confirmed for patient: " + patient.getName());
        }

        appointments.add(appointment);
        patient.addAppointment(appointment);
    }

    public BillingRecord generateBill(String id, Patient patient, double consultationFee,
                                      double labFee, double medicineFee) throws InvalidBillingException {

        BillingRecord bill = new BillingRecord(id, patient, consultationFee, labFee, medicineFee);

        if (bill.getTotalAmount() < 0) {
            throw new InvalidBillingException("Billing amount cannot be negative.");
        }

        bills.add(bill);
        patient.addBillingRecord(bill);

        auditTrail.addEntry("Bill generated for patient: " + patient.getName()
                + " amount: " + bill.getTotalAmount());

        saveBillingReceiptToFile(id, patient, consultationFee, labFee, medicineFee, bill);

        return bill;
    }

    private void saveBillingReceiptToFile(String billId, Patient patient,
                                          double consultationFee,
                                          double labFee,
                                          double medicineFee,
                                          BillingRecord bill) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("BillingReceipts.txt", true));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");
            String dateTime = LocalDateTime.now().format(formatter);

            double totalAmount = bill.getTotalAmount();

            double insuranceCovered = 0;
            double patientDue = totalAmount;

            if (patient.isInsuranceActive()) {
                insuranceCovered = totalAmount * 0.70;
                patientDue = totalAmount - insuranceCovered;
            }

            writer.write("==================================================");
            writer.newLine();
            writer.write("                 HOSPITAL BILL RECEIPT");
            writer.newLine();
            writer.write("==================================================");
            writer.newLine();
            writer.write("Date/Time          : " + dateTime);
            writer.newLine();
            writer.write("Bill ID            : " + billId);
            writer.newLine();
            writer.write("Patient ID         : " + patient.getId());
            writer.newLine();
            writer.write("Patient Name       : " + patient.getName());
            writer.newLine();
            writer.write("Department         : " + getPatientDepartmentName(patient));
            writer.newLine();
            writer.write("Insurance Active   : " + patient.isInsuranceActive());
            writer.newLine();
            writer.write("--------------------------------------------------");
            writer.newLine();
            writer.write("Consultation Fee   : PKR " + consultationFee);
            writer.newLine();
            writer.write("Lab / Test Fee     : PKR " + labFee);
            writer.newLine();
            writer.write("Medicine Fee       : PKR " + medicineFee);
            writer.newLine();
            writer.write("--------------------------------------------------");
            writer.newLine();
            writer.write("Total Amount       : PKR " + totalAmount);
            writer.newLine();
            writer.write("Insurance Covered  : PKR " + insuranceCovered);
            writer.newLine();
            writer.write("Patient Due        : PKR " + patientDue);
            writer.newLine();
            writer.write("--------------------------------------------------");
            writer.newLine();
            writer.write("Status             : Bill Generated Successfully");
            writer.newLine();
            writer.write("==================================================");
            writer.newLine();
            writer.write("        Thank you for using our hospital system");
            writer.newLine();
            writer.write("==================================================");
            writer.newLine();
            writer.newLine();

            writer.close();

            System.out.println("Billing receipt saved successfully in BillingReceipts.txt");

        } catch (IOException e) {
            System.out.println("Error: Billing receipt could not be saved.");
        }
    }

    private String getPatientDepartmentName(Patient patient) {
        if (patient.getAppointments() != null && !patient.getAppointments().isEmpty()) {
            Appointment appointment = patient.getAppointments().get(0);

            if (appointment.getDepartment() != null) {
                return appointment.getDepartment().getName();
            }
        }

        return "Not Assigned";
    }

    public void printPatientSummary(String patientId) {
        Patient patient = patients.get(patientId);

        if (patient == null) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.println("\n===== PATIENT SUMMARY =====");
        System.out.println(patient.generateReport());

        System.out.println("\nAppointments:");
        if (patient.getAppointments().isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            patient.getAppointments().forEach(a -> System.out.println(a.generateReport()));
        }

        System.out.println("\nMedical Record:");
        System.out.println(patient.getMedicalRecord().generateReport());

        System.out.println("\nBills:");
        if (patient.getBillingRecords().isEmpty()) {
            System.out.println("No bills found.");
        } else {
            patient.getBillingRecords().forEach(b -> System.out.println(b.generateReport()));
        }

        System.out.println("\nAudit Trail:");
        auditTrail.printTrail();
    }

    public void printAllAppointmentsSorted() {
        System.out.println("\n===== SORTED APPOINTMENTS =====");

        if (appointments.isEmpty()) {
            System.out.println("No appointments available.");
            return;
        }

        appointments.stream()
                .sorted(Comparator.comparing(Appointment::getDateTime))
                .forEach(a -> System.out.println(a.generateReport()));
    }

    public Optional<Patient> findPatientById(String id) {
        return Optional.ofNullable(patients.get(id));
    }

    public List<Patient> searchPatientsByName(String name) {
        List<Patient> result = new ArrayList<>();

        for (Patient patient : patients.values()) {
            if (patient.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(patient);
            }
        }

        return result;
    }

    public List<Appointment> getAppointmentsByDepartment(String departmentName) {
        List<Appointment> result = new ArrayList<>();

        appointments.stream()
                .filter(a -> a.getDepartment().getName().equalsIgnoreCase(departmentName))
                .forEach(result::add);

        return result;
    }

    public String generatePatientTextReport(String patientId) {
        Patient patient = patients.get(patientId);

        if (patient == null) {
            return "Patient not found.";
        }

        StringBuilder report = new StringBuilder();

        report.append("========================================\n");
        report.append("             PATIENT REPORT              \n");
        report.append("========================================\n\n");

        report.append("PATIENT INFORMATION\n");
        report.append("----------------------------------------\n");
        report.append(patient.generateReport()).append("\n\n");

        report.append("APPOINTMENTS\n");
        report.append("----------------------------------------\n");
        if (patient.getAppointments().isEmpty()) {
            report.append("No appointments found.\n");
        } else {
            for (Appointment appointment : patient.getAppointments()) {
                report.append(appointment.generateReport()).append("\n");
            }
        }

        report.append("\nMEDICAL RECORD\n");
        report.append("----------------------------------------\n");
        report.append(patient.getMedicalRecord().generateReport()).append("\n");

        report.append("\nBILLING RECORDS\n");
        report.append("----------------------------------------\n");
        if (patient.getBillingRecords().isEmpty()) {
            report.append("No billing records found.\n");
        } else {
            for (BillingRecord bill : patient.getBillingRecords()) {
                report.append(bill.generateReport()).append("\n");
            }
        }

        report.append("\n============= END OF PATIENT REPORT =============\n");

        return report.toString();
    }

    public String generateHospitalTextReport() {
        StringBuilder report = new StringBuilder();

        report.append("========================================\n");
        report.append("        HOSPITAL MANAGEMENT REPORT       \n");
        report.append("========================================\n\n");

        report.append("SYSTEM SUMMARY\n");
        report.append("----------------------------------------\n");
        report.append("Total Patients: ").append(patients.size()).append("\n");
        report.append("Total Staff Members: ").append(staffMembers.size()).append("\n");
        report.append("Total Departments: ").append(departments.size()).append("\n");
        report.append("Total Appointments: ").append(appointments.size()).append("\n");
        report.append("Total Bills: ").append(bills.size()).append("\n\n");

        report.append("============= PATIENT RECORDS =============\n");

        if (patients.isEmpty()) {
            report.append("No patients registered.\n");
        } else {
            for (Patient patient : patients.values()) {
                report.append("\n----------------------------------------\n");
                report.append("PATIENT INFORMATION\n");
                report.append("----------------------------------------\n");
                report.append(patient.generateReport()).append("\n");

                report.append("\nAPPOINTMENTS\n");
                report.append("----------------------------------------\n");

                if (patient.getAppointments().isEmpty()) {
                    report.append("No appointments found.\n");
                } else {
                    for (Appointment appointment : patient.getAppointments()) {
                        report.append(appointment.generateReport()).append("\n");
                    }
                }

                report.append("\nMEDICAL RECORD\n");
                report.append("----------------------------------------\n");
                report.append(patient.getMedicalRecord().generateReport()).append("\n");

                report.append("\nBILLING RECORDS\n");
                report.append("----------------------------------------\n");

                if (patient.getBillingRecords().isEmpty()) {
                    report.append("No billing records found.\n");
                } else {
                    for (BillingRecord bill : patient.getBillingRecords()) {
                        report.append(bill.generateReport()).append("\n");
                    }
                }

                report.append("----------------------------------------\n");
            }
        }

        report.append("\n============= ALL APPOINTMENTS =============\n");

        if (appointments.isEmpty()) {
            report.append("No appointments available.\n");
        } else {
            for (Appointment appointment : appointments) {
                report.append(appointment.generateReport()).append("\n");
            }
        }

        report.append("\n============= DEPARTMENTS =============\n");

        if (departments.isEmpty()) {
            report.append("No departments available.\n");
        } else {
            for (Department department : departments.values()) {
                report.append(department.generateReport()).append("\n");
            }
        }

        report.append("\n============= STAFF MEMBERS =============\n");

        if (staffMembers.isEmpty()) {
            report.append("No staff members available.\n");
        } else {
            for (Staff staff : staffMembers.values()) {
                report.append("Staff ID: ").append(staff.getId())
                        .append(", Name: ").append(staff.getName())
                        .append(", Role: ").append(staff.getRole())
                        .append(", Department: ").append(staff.getDepartmentName())
                        .append("\n");
            }
        }

        report.append("\n============= END OF REPORT =============\n");

        return report.toString();
    }

    public String generateBillingTextReport() {
        StringBuilder report = new StringBuilder();

        report.append("========================================\n");
        report.append("             BILLING RECEIPTS            \n");
        report.append("========================================\n\n");

        if (bills.isEmpty()) {
            report.append("No bills generated yet.\n");
        } else {
            for (BillingRecord bill : bills) {
                report.append(bill.generateReport()).append("\n");
                report.append("----------------------------------------\n");
            }
        }

        return report.toString();
    }

    public String generateAuditTextReport() {
        StringBuilder report = new StringBuilder();

        report.append("========================================\n");
        report.append("              AUDIT LOG REPORT           \n");
        report.append("========================================\n\n");

        if (auditTrail.getEntries().isEmpty()) {
            report.append("No audit entries available.\n");
        } else {
            for (AuditTrail.Entry entry : auditTrail.getEntries()) {
                report.append(entry.toString()).append("\n");
            }
        }

        return report.toString();
    }

    public void exportHospitalReportToFile() {
        writeTextToFile("HospitalReport.txt", generateHospitalTextReport(), false);
    }

    public void exportBillingReceiptsToFile() {
        writeTextToFile("BillingReceipts.txt", generateBillingTextReport(), false);
    }

    public void exportAuditLogToFile() {
        writeTextToFile("AuditLog.txt", generateAuditTextReport(), false);
    }

    private void writeTextToFile(String fileName, String text, boolean append) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, append));
            writer.write(text);
            writer.close();

            System.out.println(fileName + " exported successfully.");

        } catch (IOException e) {
            System.out.println("Error exporting " + fileName);
        }
    }

    public Map<String, Patient> getPatients() {
        return patients;
    }

    public Map<String, Staff> getStaffMembers() {
        return staffMembers;
    }

    public Map<String, Department> getDepartments() {
        return departments;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public List<BillingRecord> getBills() {
        return bills;
    }

    public AuditTrail getAuditTrail() {
        return auditTrail;
    }
}