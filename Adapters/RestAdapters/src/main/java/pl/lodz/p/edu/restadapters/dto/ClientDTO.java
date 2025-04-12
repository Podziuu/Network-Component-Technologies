package pl.lodz.p.edu.restadapters.dto;

import jakarta.validation.constraints.NotNull;
import model.user.ClientType;
import model.user.Role;

public class ClientDTO extends UserDTO {
    @NotNull(message = "Client type cannot be null")
    private ClientType clientType;

    public ClientDTO(String id, String login, String firstName, String lastName, Role role, ClientType clientType, boolean active) {
        super(id, login, firstName, lastName, role, active);
        this.clientType = clientType;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }
}
