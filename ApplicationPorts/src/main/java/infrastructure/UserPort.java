package infrastructure;

import model.user.Role;
import model.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
public interface UserPort {
    User addUser(User user);

    void updateUser(String id, String firstName, String lastName);

    void activateUser(String id);

    void deactivateUser(String id);

    void updatePassword(String id, String newPassword);
    User getById(String id);

    List<User> getAll();

    User findByLogin(String login);

    List<User> findByRole(Role role);

    List<User> findByFirstName(String firstName);

    List<User> findByRoleAndFirstName(Role role, String firstName);

    boolean userExists(String login);
}
