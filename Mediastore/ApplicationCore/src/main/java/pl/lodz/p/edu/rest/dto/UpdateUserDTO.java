package pl.lodz.p.edu.rest.dto;

import jakarta.validation.constraints.NotNull;
import pl.lodz.p.edu.rest.model.user.Role;

public class UpdateUserDTO {
    @NotNull(message = "Last name cannot be null")
    private String firstName;
    @NotNull(message = "Role cannot be null")
    private String lastName;

    public UpdateUserDTO() {
    }

    public UpdateUserDTO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
