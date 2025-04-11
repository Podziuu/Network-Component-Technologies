package ui;

import jakarta.validation.Valid;
import model.user.Role;
import model.user.User;

import java.util.List;

public interface IUserPort {
    User addUser(User user);

    void updateUser(String id, String firstName, String lastName);

    void activateUser(String id);

    void deactivateUser(String id);

    User getUserById(String id);

    List<User> getAllUsers();

    User getUserByLogin(String login);

    List<User> getUsersByRole(Role role);

    List<User> getUsersByFirstName(String firstName);

    List<User> getUsersByRoleAndFirstName(Role role, String firstName);

    boolean userExists(String login);

//    void changePassword(String username, dto.@Valid ChangePasswordDTO dto);
//
//    String login(dto.@Valid LoginDTO dto);
//
//    void invalidateToken(String token);
}
