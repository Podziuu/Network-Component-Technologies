package entities.user;

public class ClientEnt extends UserEnt {
    private ClientTypeEnt clientType;

    public ClientEnt(String id,
                     String login,
                     String password,
                     String firstName,
                     String lastName,
                     ClientTypeEnt clientType) {
        super(id, login, password, firstName, lastName);
        this.setRole(RoleEnt.CLIENT);
        this.clientType = clientType;
    }

    public ClientEnt() {
    }

    public ClientTypeEnt getClientType() {
        return clientType;
    }

    public void setClientType(ClientTypeEnt clientType) {
        this.clientType = clientType;
    }
}
