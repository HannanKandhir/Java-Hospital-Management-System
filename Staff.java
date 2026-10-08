package hospital.models;

public abstract class Staff extends User {
    private static final long serialVersionUID = 1L;

    private String departmentName;

    public Staff(String id, String name, String email, Role role, String departmentName) {
        super(id, name, email, role);
        this.departmentName = departmentName;
    }

    public abstract void performDuty();

    @Override
    public String getDashboardMessage() {
        return "Staff dashboard for " + getName() + " role: " + getRole();
    }

    public String getDepartmentName() {
        return departmentName;
    }
}