package services;

import exception.DuplicateUserException;
import exception.UserNotFoundException;
import exception.InvalidCredentialsException;
import infrastructure.UserCommandPort;
import model.user.Role;
import model.user.User;
import model.user.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import query.UserQueryPort;
import security.JwtTokenProvider;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService implements UserDetailsService {
    private final UserQueryPort userQueryPort;
    private final UserCommandPort userCommandPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

    @Autowired
    public UserService(UserQueryPort userQueryPort, UserCommandPort userCommandPort, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userQueryPort = userQueryPort;
        this.userCommandPort = userCommandPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public User addUser(User user) {
        if (userExists(user.getLogin())) {
            throw new DuplicateUserException("User with login " + user.getLogin() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userCommandPort.add(user);
    }

    public User getUserById(String id) {
        User user = userQueryPort.getById(id);
        if (user == null) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        return user;
    }

    public User getUserByLogin(String login) {
        User user = userQueryPort.findByLogin(login);
        if (user == null) {
            throw new UserNotFoundException("User with login " + login + " not found");
        }
        return user;
    }

    public List<User> getUsersByRole(Role role) {
        List<User> users = userQueryPort.findByRole(role);
        if (users.isEmpty()) {
            throw new UserNotFoundException("Users with role " + role + " not found");
        }
        return users;
    }

    public List<User> getAllUsers() {
        List<User> users = userQueryPort.getAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException("No users found");
        }
        return users;
    }

    public List<User> getUsersByFirstName(String firstName) {
        List<User> users = userQueryPort.findByFirstName(firstName);
        if (users.isEmpty()) {
            throw new UserNotFoundException("Users with first name " + firstName + " not found");
        }
        return users;
    }

    public List<User> getUsersByRoleAndFirstName(Role role, String firstName) {
        List<User> users = userQueryPort.findByRoleAndFirstName(role, firstName);
        if (users.isEmpty()) {
            throw new UserNotFoundException("Users with role " + role + " and first name " + firstName + " not found");
        }
        return users;
    }

    public void updateUser(String id, String firstName, String lastName) {
        if (!findUserById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        userCommandPort.update(id, firstName, lastName);
    }

    public void activateUser(String id) {
        if (!findUserById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        userCommandPort.activateUser(id);
    }

    public void deactivateUser(String id) {
        if (!findUserById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        userCommandPort.deactivateUser(id);
    }

    private boolean userExists(String login) {
        return userQueryPort.userExists(login);
    }

    private boolean findUserById(String id) {
        return userQueryPort.getById(id) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userQueryPort.findByLogin(username);
        if (user == null) {
            throw new UserNotFoundException("User with login " + username + " not found");
        }
        return new UserPrincipal(user);
    }

    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = userQueryPort.findByLogin(username);
        if (user == null) {
            throw new UserNotFoundException("User with login " + username + " not found");
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        userCommandPort.updatePassword(user.getLogin(), encodedNewPassword);
    }

    public String login(String login, String password) {
        User user = userQueryPort.findByLogin(login);
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
