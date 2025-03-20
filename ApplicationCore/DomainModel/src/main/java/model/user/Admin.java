package model.user;

public class Admin extends User {
    public Admin(String id, String login, String password, String firstName, String lastName) {
        super(id, login, password, firstName, lastName);
        this.setRole(Role.ADMIN);
    }

    public Admin() {}
}
