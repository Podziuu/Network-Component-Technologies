package pl.tks.model.user;

public class Manager extends User {
    public Manager(String id, String login, String password, String firstName, String lastName) {
        super(id, login, password, firstName, lastName);
        this.setRole(Role.MANAGER);
    }

    public Manager() {}
}
