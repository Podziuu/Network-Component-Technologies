package pl.lodz.p.edu.restadapters.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import model.user.Role;

public class UserDTO {
    private String id;
    @NotNull(message = "Login cannot be null")
    @Size(min = 3, max = 20, message = "Login must be between 3 and 20 characters")
    private String login;
    @NotNull(message = "First name cannot be null")
    private String firstName;
    @NotNull(message = "Last name cannot be null")
    private String lastName;
    @NotNull(message = "Role cannot be null")
    private Role role;
    private boolean isActive;

    @JsonCreator
    public UserDTO(String id, String login, String firstName, String lastName, Role role, boolean isActive) {
        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.isActive = isActive;
    }

    public UserDTO(String login, String firstName, String lastName, Role role) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.isActive = true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
