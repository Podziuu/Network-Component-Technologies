package dto;

import entities.user.ClientTypeEnt;
import entities.user.RoleEnt;
import jakarta.validation.constraints.NotNull;

public class ClientEntDTO extends UserEntDto {
    @NotNull(message = "Client type cannot be null")
    private ClientTypeEnt clientType;

    public ClientEntDto(String id, String login, String firstName, String lastName, RoleEnt role, ClientTypeEnt clientType, boolean active) {
        super(id, login, firstName, lastName, role, active);
        this.clientType = clientType;
    }

    public ClientTypeEnt getClientType() {
        return new ClientTypeEnt();
    }

    public void setClientType(ClientTypeEnt clientType) {
        this.clientType = clientType;
    }
}
