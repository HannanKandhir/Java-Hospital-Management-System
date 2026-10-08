package hospital;

import hospital.exceptions.*;
import hospital.models.*;
import hospital.storage.DataStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static HospitalSystem system = new HospitalSystem();
    private static Scanner input = new Scanner(System.in);

    private static Department cardiology;
    private static Specialist specialist;
    private static Pharmacist pharmacist;
    private static LabTechnician labTech;
    private static Administrator admin;

    private static Role currentRole = null;

    public static void main(String[] args) {

        setupDefaultHospitalData();

        int choice;

        do {
            if (currentRole == null) {
                loginMenu();
            }

            showDashboard();
            showMainMenu();

            choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1:
                        if (hasAccess(Role.ADMINISTRATOR)) {
                            patientManagementMenu();
                        }
                        break;

                    case 2:
                        if (hasAccess(Role.ADMINISTRATOR, Role.GENERAL_PHYSICIAN, Role.SPECIALIST)) {
                            appointmentManagementMenu();
                        }
                        break;

                    case 3:
                        if (hasAccess(Role.GENERAL_PHYSICIAN, Role.SPECIALIST)) {
                            clinicalManagementMenu();
                        }
                        break;

                    case 4:
                        if (hasAccess(Role.GENERAL_PHYSICIAN, Role.SPECIALIST, Role.PHARMACIST)) {
                            pharmacyManagementMenu();
                        }
                        break;

                    case 5:
                        if (hasAccess(Role.GENERAL_PHYSICIAN, Role.SPECIALIST, Role.LAB_TECHNICIAN)) {
                            laboratoryManagementMenu();
                        }
                        break;

                    case 6:
                        if (hasAccess(Role.ADMINISTRATOR)) {
                            billingManagementMenu();
                        }
                        break;

                    case 7:
                        reportsMenu();
                        break;

                    case 8:
                        if (hasAccess(Role.ADMINISTRATOR)) {
                            DataStorage.save(system, "HospitalSystem.ser");
                        }
                        break;

                    case 9:
                        if (hasAccess(Role.ADMINISTRATOR)) {
                            system = DataStorage.load("HospitalSystem.ser");
                            setupDefaultHospitalData();
                            System.out.println("Data loaded successfully.");
                        }
                        break;

                    case 10:
                        if (hasAccess(Role.ADMINISTRATOR)) {
                            exportReportsToTextFile();
                        }
                        break;

                    case 11:
                        if (hasAccess(Role.ADMINISTRATOR)) {
                            loadSampleDemoData();
                        }
                        break;

                    case 12:
                        currentRole = null;
                        System.out.println("Logged out successfully.");
                        break;

                    case 0:
                        System.out.println("Thank you for using Hospital Management System.");
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }

        } while (choice != 0);
    }

    private static void setupDefaultHospitalData() {
        try {
            cardiology = new Department("DEP-01", "Cardiology", 10);

            specialist = new Specialist(
                    "D105",
                    "Dr. Khalid Raza",
                    "khalid@hospital.com",
                    "Cardiologist",
                    "Cardiology"
            );

            pharmacist = new Pharmacist(
                    "PH-01",
                    "Ali Pharmacist",
                    "pharmacy@hospital.com"
            );

            labTech = new LabTechnician(
                    "LT-07",
                    "Sara LabTech",
                    "lab@hospital.com"
            );

            admin = new Administrator(
                    "AD-01",
                    "Hospital Admin",
                    "admin@hospital.com"
            );

            if (!system.getDepartments().containsKey(cardiology.getId())) {
                system.addDepartment(cardiology);
            }

            if (!system.getStaffMembers().containsKey(specialist.getId())) {
                system.addStaff(specialist);
            }

            if (!system.getStaffMembers().containsKey(pharmacist.getId())) {
                system.addStaff(pharmacist);
            }

            if (!system.getStaffMembers().containsKey(labTech.getId())) {
                system.addStaff(labTech);
            }

            if (!system.getStaffMembers().containsKey(admin.getId())) {
                system.addStaff(admin);
            }

        } catch (DuplicateRecordException e) {
            System.out.println("Default hospital data already exists.");
        }
    }

    private static void loginMenu() {
        int choice;

        do {
            System.out.println("\n========================================");
            System.out.println("              SYSTEM LOGIN              ");
            System.out.println("========================================");
            System.out.println("1. Administrator");
            System.out.println("2. Doctor");
            System.out.println("3. Pharmacist");
            System.out.println("4. Lab Technician");
            System.out.println("5. Nurse");
            System.out.println("6. Patient");
            System.out.println("0. Exit");
            System.out.println("========================================");

            choice = readInt("Login as: ");

            switch (choice) {
                case 1:
                    currentRole = Role.ADMINISTRATOR;
                    System.out.println("Logged in as Administrator.");
                    break;

                case 2:
                    currentRole = Role.SPECIALIST;
                    System.out.println("Logged in as Doctor.");
                    break;

                case 3:
                    currentRole = Role.PHARMACIST;
                    System.out.println("Logged in as Pharmacist.");
                    break;

                case 4:
                    currentRole = Role.LAB_TECHNICIAN;
                    System.out.println("Logged in as Lab Technician.");
                    break;

                case 5:
                    currentRole = Role.NURSE;
                    System.out.println("Logged in as Nurse.");
                    break;

                case 6:
                    currentRole = Role.PATIENT;
                    System.out.println("Logged in as Patient.");
                    break;

                case 0:
                    System.out.println("Exiting system.");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid login choice.");
            }

        } while (currentRole == null);
    }

    private static boolean hasAccess(Role... allowedRoles) {
        for (Role role : allowedRoles) {
            if (currentRole == role) {
                return true;
            }
        }

        System.out.println("Access denied. This option is not allowed for your role.");
        return false;
    }

    private static void showDashboard() {
        System.out.println("\n========================================");
        System.out.println("        HOSPITAL SYSTEM DASHBOARD        ");
        System.out.println("================================--------");
        System.out.println("Logged In Role: " + currentRole);
        System.out.println("Patients Registered: " + system.getPatients().size());
        System.out.println("Appointments Booked: " + system.getAppointments().size());
        System.out.println("Bills Generated: " + system.getBills().size());
        System.out.println("System Status: Running");
        System.out.println("========================================");
    }

    private static void showMainMenu() {
        System.out.println("\n========================================");
        System.out.println("        HOSPITAL MANAGEMENT SYSTEM       ");
        System.out.println("========================================");

        if (currentRole == Role.ADMINISTRATOR) {
            System.out.println("1. Patient Management");
            System.out.println("2. Appointment Management");
            System.out.println("6. Billing Management");
            System.out.println("7. Reports");
            System.out.println("8. Save Data");
            System.out.println("9. Load Data");
            System.out.println("10. Export TXT Reports");
            System.out.println("11. Load Sample Demo Data");
        } else if (currentRole == Role.SPECIALIST || currentRole == Role.GENERAL_PHYSICIAN) {
            System.out.println("2. Appointment Management");
            System.out.println("3. Clinical Management");
            System.out.println("4. Pharmacy / Prescription Management");
            System.out.println("5. Laboratory Management");
            System.out.println("7. Reports");
        } else if (currentRole == Role.PHARMACIST) {
            System.out.println("4. Pharmacy Management");
            System.out.println("7. Reports");
        } else if (currentRole == Role.LAB_TECHNICIAN) {
            System.out.println("5. Laboratory Management");
            System.out.println("7. Reports");
        } else if (currentRole == Role.NURSE || currentRole == Role.PATIENT) {
            System.out.println("7. Reports");
        }

        System.out.println("12. Logout");
        System.out.println("0. Exit");
        System.out.println("========================================");
    }

    private static void patientManagementMenu() throws DuplicateRecordException {
        int choice;

        do {
            System.out.println("\n===== PATIENT MANAGEMENT =====");
            System.out.println("1. Register New Patient");
            System.out.println("2. View All Patients");
            System.out.println("3. Search Patient by Name");
            System.out.println("4. View Patient Summary");
            System.out.println("0. Back");

            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    registerPatient();
                    break;

                case 2:
                    viewAllPatients();
                    break;

                case 3:
                    searchPatientByName();
                    break;

                case 4:
                    viewPatientSummary();
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }

    private static void appointmentManagementMenu()
            throws AppointmentConflictException, InvalidBillingException {

        int choice;

        do {
            System.out.println("\n===== APPOINTMENT MANAGEMENT =====");
            System.out.println("1. Book Appointment");
            System.out.println("2. View All Appointments");
            System.out.println("0. Back");

            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    bookAppointment();
                    break;

                case 2:
                    system.printAllAppointmentsSorted();
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }

    private static void clinicalManagementMenu() throws UnauthorizedAccessException {
        int choice;

        do {
            System.out.println("\n===== CLINICAL MANAGEMENT =====");
            System.out.println("1. Add Diagnosis");
            System.out.println("2. Create Treatment Plan");
            System.out.println("3. View Medical Record");
            System.out.println("0. Back");

            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    addDiagnosis();
                    break;

                case 2:
                    createTreatmentPlan();
                    break;

                case 3:
                    viewPatientSummary();
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }

    private static void pharmacyManagementMenu()
            throws UnauthorizedAccessException, DrugInteractionException {

        int choice;

        do {
            System.out.println("\n===== PHARMACY / PRESCRIPTION MANAGEMENT =====");
            System.out.println("1. Issue Prescription and Dispense Medicine");
            System.out.println("0. Back");

            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    issuePrescription();
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }

    private static void laboratoryManagementMenu() throws UnauthorizedAccessException {
        int choice;

        do {
            System.out.println("\n===== LABORATORY MANAGEMENT =====");
            System.out.println("1. Create Lab Order and Upload Result");
            System.out.println("0. Back");

            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    createLabOrderAndUploadResult();
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }

    private static void billingManagementMenu() throws InvalidBillingException {
        int choice;

        do {
            System.out.println("\n===== BILLING MANAGEMENT =====");
            System.out.println("1. Generate Bill");
            System.out.println("2. View Patient Billing Summary");
            System.out.println("0. Back");

            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    generateBill();
                    break;

                case 2:
                    viewPatientSummary();
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }

    private static void reportsMenu() throws UnauthorizedAccessException {
        int choice;

        do {
            System.out.println("\n===== REPORTS =====");
            System.out.println("1. View Patient Summary");
            System.out.println("2. View All Patients");
            System.out.println("3. View All Appointments");
            System.out.println("4. Export One Patient Report");
            System.out.println("5. Export Full TXT Reports");

            if (currentRole == Role.ADMINISTRATOR) {
                System.out.println("6. Discharge Patient");
            }

            System.out.println("0. Back");

            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    viewPatientSummary();
                    break;

                case 2:
                    viewAllPatients();
                    break;

                case 3:
                    system.printAllAppointmentsSorted();
                    break;

                case 4:
                    exportOnePatientReport();
                    break;

                case 5:
                    exportReportsToTextFile();
                    break;

                case 6:
                    if (currentRole == Role.ADMINISTRATOR) {
                        dischargePatient();
                    } else {
                        System.out.println("Access denied.");
                    }
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }

    private static String generatePatientId() {
        int number = 1001 + system.getPatients().size();
        String id = "P-" + number;

        while (system.getPatients().containsKey(id)) {
            number++;
            id = "P-" + number;
        }

        return id;
    }

    private static void registerPatient() throws DuplicateRecordException {
        System.out.println("\n===== REGISTER NEW PATIENT =====");

        String id = generatePatientId();
        System.out.println("Auto Generated Patient ID: " + id);

        String name = readText("Enter Patient Name: ");
        if (name.isBlank()) {
            System.out.println("Patient name cannot be empty.");
            return;
        }

        int age = readInt("Enter Age: ");
        if (age <= 0) {
            System.out.println("Age must be greater than 0.");
            return;
        }

        System.out.println("\nSelect Department:");
        System.out.println("1. Cardiology");
        System.out.println("2. General Medicine");
        System.out.println("3. Emergency");
        System.out.println("4. Laboratory");

        int departmentChoice = readInt("Choose department: ");
        String department;

        switch (departmentChoice) {
            case 1:
                department = "Cardiology";
                break;

            case 2:
                department = "General Medicine";
                break;

            case 3:
                department = "Emergency";
                break;

            case 4:
                department = "Laboratory";
                break;

            default:
                System.out.println("Invalid department selected.");
                return;
        }

        System.out.println("\nSelect Medical History:");
        System.out.println("1. No major illness");
        System.out.println("2. Hypertension");
        System.out.println("3. Diabetes");
        System.out.println("4. Heart disease");
        System.out.println("5. Custom");

        int historyChoice = readInt("Choose medical history: ");
        String history;

        switch (historyChoice) {
            case 1:
                history = "No major illness";
                break;

            case 2:
                history = "Hypertension";
                break;

            case 3:
                history = "Diabetes";
                break;

            case 4:
                history = "Heart disease";
                break;

            case 5:
                history = readText("Enter custom medical history: ");
                if (history.isBlank()) {
                    history = "No medical history provided";
                }
                break;

            default:
                System.out.println("Invalid medical history selected.");
                return;
        }

        System.out.println("\nInsurance Status:");
        System.out.println("1. Active");
        System.out.println("2. Not Active");

        int insuranceChoice = readInt("Choose insurance status: ");
        boolean insuranceActive;

        if (insuranceChoice == 1) {
            insuranceActive = true;
        } else if (insuranceChoice == 2) {
            insuranceActive = false;
        } else {
            System.out.println("Invalid insurance choice.");
            return;
        }

        Patient patient = new Patient(id, name, age, department, history, insuranceActive);
        system.registerPatient(patient);

        System.out.println("\nPatient registered successfully.");
        System.out.println("Patient ID: " + patient.getId());
        System.out.println("Name: " + patient.getName());
    }

    private static void viewAllPatients() {
        System.out.println("\n===== ALL PATIENTS =====");

        if (system.getPatients().isEmpty()) {
            System.out.println("No patients registered yet.");
            return;
        }

        int number = 1;

        for (Patient patient : system.getPatients().values()) {
            System.out.println(number + ". "
                    + patient.getId()
                    + " - "
                    + patient.getName()
                    + " - "
                    + patient.getHomeDepartment());
            number++;
        }
    }

    private static void searchPatientByName() {
        System.out.println("\n===== SEARCH PATIENT BY NAME =====");

        String name = readText("Enter patient name: ");

        if (name.isBlank()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        List<Patient> result = system.searchPatientsByName(name);

        if (result.isEmpty()) {
            System.out.println("No patient found with this name.");
            return;
        }

        System.out.println("\nSearch Results:");
        for (Patient patient : result) {
            System.out.println(patient.getId() + " - " + patient.getName() + " - " + patient.getHomeDepartment());
        }
    }

    private static Patient selectPatient() {
        if (system.getPatients().isEmpty()) {
            System.out.println("No patients available. Please register a patient first.");
            return null;
        }

        List<Patient> patientList = new ArrayList<>(system.getPatients().values());

        System.out.println("\nAvailable Patients:");
        for (int i = 0; i < patientList.size(); i++) {
            Patient p = patientList.get(i);
            System.out.println((i + 1) + ". " + p.getId() + " - " + p.getName());
        }

        int choice = readInt("Choose patient: ");

        if (choice < 1 || choice > patientList.size()) {
            System.out.println("Invalid patient choice.");
            return null;
        }

        return patientList.get(choice - 1);
    }

    private static void bookAppointment()
            throws AppointmentConflictException, InvalidBillingException {

        System.out.println("\n===== BOOK APPOINTMENT =====");

        Patient patient = selectPatient();

        if (patient == null) {
            return;
        }

        System.out.println("\nAvailable Appointment Slots:");
        System.out.println("1. 2026-06-23 10:30 AM");
        System.out.println("2. 2026-06-23 11:00 AM");
        System.out.println("3. 2026-06-24 09:30 AM");
        System.out.println("4. 2026-06-24 12:00 PM");

        int slotChoice = readInt("Choose appointment slot: ");

        LocalDateTime appointmentTime;

        switch (slotChoice) {
            case 1:
                appointmentTime = LocalDateTime.of(2026, 6, 23, 10, 30);
                break;

            case 2:
                appointmentTime = LocalDateTime.of(2026, 6, 23, 11, 0);
                break;

            case 3:
                appointmentTime = LocalDateTime.of(2026, 6, 24, 9, 30);
                break;

            case 4:
                appointmentTime = LocalDateTime.of(2026, 6, 24, 12, 0);
                break;

            default:
                System.out.println("Invalid appointment slot.");
                return;
        }

        String appointmentId = "A-" + System.currentTimeMillis();

        Appointment appointment = new Appointment(
                appointmentId,
                patient,
                specialist,
                cardiology,
                appointmentTime,
                AppointmentType.SPECIALIST_REFERRAL
        );

        system.bookAppointment(appointment);

        System.out.println("\nAppointment booked successfully.");
        System.out.println("Appointment ID: " + appointmentId);
        System.out.println("Patient: " + patient.getName());
        System.out.println("Doctor: " + specialist.getName());
        System.out.println("Time: " + appointmentTime);
    }

    private static void addDiagnosis() throws UnauthorizedAccessException {
        System.out.println("\n===== ADD DIAGNOSIS =====");

        Patient patient = selectPatient();

        if (patient == null) {
            return;
        }

        System.out.println("\nSelect Diagnosis:");
        System.out.println("1. Hypertension follow-up. Patient is stable.");
        System.out.println("2. Chest pain evaluation required.");
        System.out.println("3. Diabetes monitoring required.");
        System.out.println("4. Routine checkup completed.");
        System.out.println("5. Custom diagnosis");

        int choice = readInt("Choose diagnosis: ");
        String diagnosis;

        switch (choice) {
            case 1:
                diagnosis = "Hypertension follow-up. Patient is stable.";
                break;

            case 2:
                diagnosis = "Chest pain evaluation required.";
                break;

            case 3:
                diagnosis = "Diabetes monitoring required.";
                break;

            case 4:
                diagnosis = "Routine checkup completed.";
                break;

            case 5:
                diagnosis = readText("Enter custom diagnosis: ");
                if (diagnosis.isBlank()) {
                    System.out.println("Diagnosis cannot be empty.");
                    return;
                }
                break;

            default:
                System.out.println("Invalid diagnosis choice.");
                return;
        }

        specialist.addDiagnosis(patient, diagnosis);

        System.out.println("\nDiagnosis added successfully.");
    }

    private static void createTreatmentPlan() throws UnauthorizedAccessException {
        System.out.println("\n===== CREATE TREATMENT PLAN =====");

        Patient patient = selectPatient();

        if (patient == null) {
            return;
        }

        System.out.println("\nSelect Treatment Plan:");
        System.out.println("1. Continue medication and follow up after 2 weeks.");
        System.out.println("2. Lifestyle changes and follow up after 1 month.");
        System.out.println("3. Lab tests required before next visit.");
        System.out.println("4. Custom treatment plan");

        int choice = readInt("Choose treatment plan: ");

        String description;
        String instructions;

        switch (choice) {
            case 1:
                description = "Continue current medication.";
                instructions = "Follow up after 2 weeks.";
                break;

            case 2:
                description = "Lifestyle changes recommended.";
                instructions = "Follow up after 1 month.";
                break;

            case 3:
                description = "Diagnostic tests required.";
                instructions = "Complete lab tests before next visit.";
                break;

            case 4:
                description = readText("Enter treatment description: ");
                instructions = readText("Enter follow-up instructions: ");

                if (description.isBlank() || instructions.isBlank()) {
                    System.out.println("Treatment description and instructions cannot be empty.");
                    return;
                }
                break;

            default:
                System.out.println("Invalid treatment plan choice.");
                return;
        }

        String planId = "TP-" + System.currentTimeMillis();

        specialist.createTreatmentPlan(
                planId,
                patient,
                description,
                instructions
        );

        System.out.println("\nTreatment plan created successfully.");
        System.out.println("Treatment Plan ID: " + planId);
    }

    private static void issuePrescription()
            throws UnauthorizedAccessException, DrugInteractionException {

        System.out.println("\n===== ISSUE PRESCRIPTION =====");

        Patient patient = selectPatient();

        if (patient == null) {
            return;
        }

        System.out.println("\nSelect Medicine:");
        System.out.println("1. Amlodipine - 5mg once daily");
        System.out.println("2. Paracetamol - 500mg twice daily");
        System.out.println("3. Metformin - 500mg once daily");
        System.out.println("4. Atorvastatin - 10mg once daily");
        System.out.println("5. Custom Medicine");

        int choice = readInt("Choose medicine: ");

        String medicine;
        String dosage;

        switch (choice) {
            case 1:
                medicine = "Amlodipine";
                dosage = "5mg once daily";
                break;

            case 2:
                medicine = "Paracetamol";
                dosage = "500mg twice daily";
                break;

            case 3:
                medicine = "Metformin";
                dosage = "500mg once daily";
                break;

            case 4:
                medicine = "Atorvastatin";
                dosage = "10mg once daily";
                break;

            case 5:
                medicine = readText("Enter Medicine Name: ");
                dosage = readText("Enter Dosage: ");

                if (medicine.isBlank() || dosage.isBlank()) {
                    System.out.println("Medicine name and dosage cannot be empty.");
                    return;
                }
                break;

            default:
                System.out.println("Invalid medicine choice.");
                return;
        }

        String prescriptionId = "RX-" + System.currentTimeMillis();

        Prescription prescription = specialist.issuePrescription(
                prescriptionId,
                patient,
                medicine,
                dosage
        );

        pharmacist.validateAndDispense(patient, prescription);

        System.out.println("\nPrescription issued successfully.");
        System.out.println("Prescription ID: " + prescriptionId);
        System.out.println("Medicine: " + medicine);
        System.out.println("Dosage: " + dosage);
        System.out.println("Status: Dispensed by pharmacist");
    }

    private static void createLabOrderAndUploadResult()
            throws UnauthorizedAccessException {

        System.out.println("\n===== LAB ORDER AND RESULT =====");

        Patient patient = selectPatient();

        if (patient == null) {
            return;
        }

        System.out.println("\nSelect Lab Test:");
        System.out.println("1. ECG");
        System.out.println("2. Lipid Panel");
        System.out.println("3. Blood Sugar Test");
        System.out.println("4. ECG + Lipid Panel");
        System.out.println("5. Custom Test");

        int testChoice = readInt("Choose lab test: ");

        String testName;
        String resultDetails;

        switch (testChoice) {
            case 1:
                testName = "ECG";
                resultDetails = "ECG Normal Sinus Rhythm.";
                break;

            case 2:
                testName = "Lipid Panel";
                resultDetails = "LDL 142 mg/dL. Borderline high.";
                break;

            case 3:
                testName = "Blood Sugar Test";
                resultDetails = "Blood sugar level needs monitoring.";
                break;

            case 4:
                testName = "ECG + Lipid Panel";
                resultDetails = "ECG Normal Sinus Rhythm; LDL 142 mg/dL borderline high.";
                break;

            case 5:
                testName = readText("Enter Test Name: ");
                resultDetails = readText("Enter Result Details: ");

                if (testName.isBlank() || resultDetails.isBlank()) {
                    System.out.println("Test name and result details cannot be empty.");
                    return;
                }
                break;

            default:
                System.out.println("Invalid lab test choice.");
                return;
        }

        String orderId = "LO-" + System.currentTimeMillis();
        String resultId = "R-" + System.currentTimeMillis();

        LabOrder order = specialist.createLabOrder(
                orderId,
                patient,
                testName,
                "Routine"
        );

        labTech.uploadResult(
                resultId,
                order,
                resultDetails
        );

        System.out.println("\nLab order created and result uploaded successfully.");
        System.out.println("Lab Order ID: " + orderId);
        System.out.println("Test: " + testName);
        System.out.println("Result: " + resultDetails);
    }

    private static void generateBill() throws InvalidBillingException {
        System.out.println("\n===== GENERATE BILL =====");

        Patient patient = selectPatient();

        if (patient == null) {
            return;
        }

        System.out.println("\nSelect Billing Package:");
        System.out.println("1. Consultation Only - PKR 2000");
        System.out.println("2. Consultation + ECG - PKR 2800");
        System.out.println("3. Consultation + Lab + Medicine - PKR 4450");
        System.out.println("4. Emergency Package - PKR 6000");
        System.out.println("5. Custom Bill");

        int choice = readInt("Choose billing package: ");

        double consultationFee = 0;
        double labFee = 0;
        double medicineFee = 0;

        switch (choice) {
            case 1:
                consultationFee = 2000;
                labFee = 0;
                medicineFee = 0;
                break;

            case 2:
                consultationFee = 2000;
                labFee = 800;
                medicineFee = 0;
                break;

            case 3:
                consultationFee = 2000;
                labFee = 2000;
                medicineFee = 450;
                break;

            case 4:
                consultationFee = 3000;
                labFee = 2000;
                medicineFee = 1000;
                break;

            case 5:
                consultationFee = readDouble("Enter Consultation Fee: ");
                labFee = readDouble("Enter Lab Fee: ");
                medicineFee = readDouble("Enter Medicine Fee: ");

                if (consultationFee < 0 || labFee < 0 || medicineFee < 0) {
                    System.out.println("Fees cannot be negative.");
                    return;
                }
                break;

            default:
                System.out.println("Invalid billing package.");
                return;
        }

        String billingId = "B-" + System.currentTimeMillis();

        BillingRecord bill = system.generateBill(
                billingId,
                patient,
                consultationFee,
                labFee,
                medicineFee
        );

        System.out.println("\nBill generated successfully.");
        System.out.println(bill.generateReport());
    }

    private static void dischargePatient() throws UnauthorizedAccessException {
        System.out.println("\n===== DISCHARGE PATIENT =====");

        Patient patient = selectPatient();

        if (patient == null) {
            return;
        }

        if (patient.isDischarged()) {
            System.out.println("This patient is already discharged.");
            return;
        }

        if (patient.getBillingRecords().isEmpty()) {
            System.out.println("Cannot discharge patient before billing is generated.");
            return;
        }

        admin.dischargePatient(patient);

        System.out.println("\nPatient discharged successfully.");
        System.out.println("Patient: " + patient.getName());
    }

    private static void viewPatientSummary() {
        System.out.println("\n===== PATIENT SUMMARY =====");

        Patient patient = selectPatient();

        if (patient == null) {
            return;
        }

        system.printPatientSummary(patient.getId());
    }

    private static void exportReportsToTextFile() {
        System.out.println("\n===== EXPORT TXT REPORTS =====");

        String hospitalReport = system.generateHospitalTextReport();
        String billingReport = system.generateBillingTextReport();
        String auditReport = system.generateAuditTextReport();

        DataStorage.exportTextFile("HospitalReport.txt", hospitalReport);
        DataStorage.exportTextFile("BillingReceipts.txt", billingReport);
        DataStorage.exportTextFile("AuditLog.txt", auditReport);

        System.out.println("\nAll TXT reports exported successfully.");
        System.out.println("Files created:");
        System.out.println("1. HospitalReport.txt");
        System.out.println("2. BillingReceipts.txt");
        System.out.println("3. AuditLog.txt");
    }

    private static void exportOnePatientReport() {
        System.out.println("\n===== EXPORT ONE PATIENT REPORT =====");

        Patient patient = selectPatient();

        if (patient == null) {
            return;
        }

        String report = system.generatePatientTextReport(patient.getId());
        String fileName = "PatientReport_" + patient.getId().replace("-", "_") + ".txt";

        DataStorage.exportTextFile(fileName, report);

        System.out.println("Patient report exported successfully.");
        System.out.println("File created: " + fileName);
    }

    private static void loadSampleDemoData() {
        try {
            System.out.println("\n===== LOAD SAMPLE DEMO DATA =====");

            String id = generatePatientId();

            Patient patient = new Patient(
                    id,
                    "Sara Ahmed",
                    34,
                    "Cardiology",
                    "Hypertension",
                    true
            );

            system.registerPatient(patient);

            Appointment appointment = new Appointment(
                    "A-" + System.currentTimeMillis(),
                    patient,
                    specialist,
                    cardiology,
                    LocalDateTime.of(2026, 6, 25, 10, 30),
                    AppointmentType.SPECIALIST_REFERRAL
            );

            system.bookAppointment(appointment);

            specialist.addDiagnosis(patient, "Hypertension follow-up. Patient is stable.");

            specialist.createTreatmentPlan(
                    "TP-" + System.currentTimeMillis(),
                    patient,
                    "Continue blood pressure monitoring.",
                    "Follow up after 2 weeks."
            );

            Prescription prescription = specialist.issuePrescription(
                    "RX-" + System.currentTimeMillis(),
                    patient,
                    "Amlodipine",
                    "5mg once daily"
            );

            pharmacist.validateAndDispense(patient, prescription);

            LabOrder order = specialist.createLabOrder(
                    "LO-" + System.currentTimeMillis(),
                    patient,
                    "ECG + Lipid Panel",
                    "Routine"
            );

            labTech.uploadResult(
                    "R-" + System.currentTimeMillis(),
                    order,
                    "ECG Normal Sinus Rhythm; LDL 142 mg/dL borderline high."
            );

            system.generateBill(
                    "B-" + System.currentTimeMillis(),
                    patient,
                    2000,
                    2000,
                    450
            );

            admin.dischargePatient(patient);

            System.out.println("Sample demo data loaded successfully.");
            System.out.println("Demo Patient ID: " + patient.getId());
            System.out.println("Demo Patient Name: " + patient.getName());

        } catch (Exception e) {
            System.out.println("Error loading sample data: " + e.getMessage());
        }
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);

            if (input.hasNextInt()) {
                int value = input.nextInt();
                input.nextLine();
                return value;
            } else {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine();
            }
        }
    }

    private static double readDouble(String message) {
        while (true) {
            System.out.print(message);

            if (input.hasNextDouble()) {
                double value = input.nextDouble();
                input.nextLine();
                return value;
            } else {
                System.out.println("Invalid input. Please enter a valid amount.");
                input.nextLine();
            }
        }
    }

    private static String readText(String message) {
        System.out.print(message);
        return input.nextLine();
    }
}
