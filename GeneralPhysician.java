package hospital.models;

public class GeneralPhysician extends Doctor {
    private static final long serialVersionUID = 1L;

    public GeneralPhysician(String id, String name, String email, String departmentName) {
        super(id, name, email, Role.GENERAL_PHYSICIAN, "General Medicine", departmentName);
    }

    @Override
    public void performDuty() {
        System.out.println(getName() + " is conducting general consultation.");
    }
}