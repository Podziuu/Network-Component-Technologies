package pl.tks.rest.dto;

import jakarta.validation.constraints.NotNull;
import pl.tks.model.user.ClientType;
import pl.tks.model.user.Role;

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
