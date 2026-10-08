package hospital.models;

public class Specialist extends Doctor {
    private static final long serialVersionUID = 1L;

    public Specialist(String id, String name, String email,
                      String specialization, String departmentName) {
        super(id, name, email, Role.SPECIALIST, specialization, departmentName);
    }

    @Override
    public void performDuty() {
        System.out.println(getName() + " is handling specialist treatment.");
    }
}