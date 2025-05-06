package pl.tks.ports.ui;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.tks.model.user.Role;
import pl.tks.model.user.User;

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

    boolean isTokenOnBlackList(String token);

//    void changePassword(String username, dto.@Valid ChangePasswordDTO dto);

    String login(String login, String password);

    void invalidateToken(String token);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
