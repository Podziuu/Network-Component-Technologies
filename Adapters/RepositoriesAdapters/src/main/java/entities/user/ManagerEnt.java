package entities.user;

public class ManagerEnt extends UserEnt {
    public ManagerEnt(String id, String login, String password, String firstName, String lastName) {
        super(id, login, password, firstName, lastName);
        this.setRole(RoleEnt.MANAGER);
    }

    public ManagerEnt() {
    }
}
