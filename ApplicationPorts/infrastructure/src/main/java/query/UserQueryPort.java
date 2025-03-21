package query;

import model.user.Role;
import model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserQueryPort {
    User getById(String id);

    List<User> getAll();

    User findByLogin(String login);

    List<User> findByRole(Role role);

    List<User> findByFirstName(String firstName);

    List<User> findByRoleAndFirstName(Role role, String firstName);

    boolean userExists(String login);
}
