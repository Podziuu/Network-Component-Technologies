package services;

import exception.DuplicateUserException;
import exception.UserNotFoundException;
import exception.InvalidCredentialsException;
import infrastructure.UserPort;
import model.user.Role;
import model.user.User;
import model.user.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import security.JwtTokenProvider;
import ui.IUserPort;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService implements IUserPort, UserDetailsService {
    private final UserPort userPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

    @Autowired
    public UserService(UserPort userPort, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userPort = userPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public User addUser(User user) {
        if (userExists(user.getLogin())) {
            throw new DuplicateUserException("User with login " + user.getLogin() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userPort.addUser(user);
    }

    public User getUserById(String id) {
        User user = userPort.getById(id);
        if (user == null) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        return user;
    }

    public User getUserByLogin(String login) {
        User user = userPort.findByLogin(login);
        if (user == null) {
            throw new UserNotFoundException("User with login " + login + " not found");
        }
        return user;
    }

    public List<User> getUsersByRole(Role role) {
        List<User> users = userPort.findByRole(role);
        if (users.isEmpty()) {
            throw new UserNotFoundException("Users with role " + role + " not found");
        }
        return users;
    }

    public List<User> getAllUsers() {
        List<User> users = userPort.getAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException("No users found");
        }
        return users;
    }

    public List<User> getUsersByFirstName(String firstName) {
        List<User> users = userPort.findByFirstName(firstName);
        if (users.isEmpty()) {
            throw new UserNotFoundException("Users with first name " + firstName + " not found");
        }
        return users;
    }

    public List<User> getUsersByRoleAndFirstName(Role role, String firstName) {
        List<User> users = userPort.findByRoleAndFirstName(role, firstName);
        if (users.isEmpty()) {
            throw new UserNotFoundException("Users with role " + role + " and first name " + firstName + " not found");
        }
        return users;
    }

    public void updateUser(String id, String firstName, String lastName) {
        if (!findUserById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        userPort.updateUser(id, firstName, lastName);
    }

    public void activateUser(String id) {
        if (!findUserById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        userPort.activateUser(id);
    }

    public void deactivateUser(String id) {
        if (!findUserById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        userPort.deactivateUser(id);
    }

    public boolean userExists(String login) {
        return userPort.userExists(login);
    }

    private boolean findUserById(String id) {
        return userPort.getById(id) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userPort.findByLogin(username);
        if (user == null) {
            throw new UserNotFoundException("User with login " + username + " not found");
        }
        return new UserPrincipal(user);
    }

    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = userPort.findByLogin(username);
        if (user == null) {
            throw new UserNotFoundException("User with login " + username + " not found");
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        userPort.updatePassword(user.getLogin(), encodedNewPassword);
    }

    public String login(String login, String password) {
        User user = userPort.findByLogin(login);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid login or password");
        }
        return jwtTokenProvider.generateToken(user.getLogin(), user.getId(), user.getRole());
    }

    public void invalidateToken(String token) {
        blacklist.add(token);
    }

    public boolean isTokenOnBlackList(String token) {
        return blacklist.contains(token);
    }
}
