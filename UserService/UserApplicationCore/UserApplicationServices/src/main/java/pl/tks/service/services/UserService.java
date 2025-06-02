package pl.tks.service.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.tks.event.ClientCreatedEvent;
import pl.tks.messaging.ClientProducer;
import pl.tks.model.user.Client;
import pl.tks.model.user.Role;
import pl.tks.model.user.User;
import pl.tks.model.user.UserPrincipal;
import pl.tks.ports.infrastructure.RollbackHandlerPort;
import pl.tks.ports.infrastructure.TokenProviderPort;
import pl.tks.ports.infrastructure.UserPort;
import pl.tks.ports.ui.IUserPort;
import pl.tks.service.exception.DuplicateUserException;
import pl.tks.service.exception.InvalidCredentialsException;
import pl.tks.service.exception.UserNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService implements IUserPort, UserDetailsService, RollbackHandlerPort {
    private final UserPort userPort;
    private final PasswordEncoder passwordEncoder;
    private final TokenProviderPort tokenProviderPort;
    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();
    private final ClientProducer clientProducer;
    private final Map<String, String> rollbackNotifications = new ConcurrentHashMap<>();

    public UserService(UserPort userPort, PasswordEncoder passwordEncoder, TokenProviderPort tokenProviderPort, ClientProducer clientProducer) {
        this.userPort = userPort;
        this.passwordEncoder = passwordEncoder;
        this.tokenProviderPort = tokenProviderPort;
        this.clientProducer = clientProducer;
    }

    public User addUser(User user) {
        if (userExists(user.getLogin())) {
            throw new DuplicateUserException("User with login " + user.getLogin() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = userPort.addUser(user);

        try {
            if (createdUser instanceof Client) {
                ClientCreatedEvent event = new ClientCreatedEvent(createdUser.getId(), createdUser.getFirstName(),
                        createdUser.getLastName(), 2, 0);
                clientProducer.sendCreateClientEvent(event);

                rollbackNotifications.put(createdUser.getId(), "User created");

                System.out.println("=== USERSERVICE: Sent client creation event for: " + createdUser.getId() + " - " +
                        createdUser.getFirstName() + " " + createdUser.getLastName() + " ===");
            }
        } catch (Exception e) {
            if (createdUser.getId() != null) {
                try {
                    userPort.deleteById(createdUser.getId());
                    rollbackNotifications.put(createdUser.getId(), "User creation failed during messaging: " + e.getMessage());
                    System.err.println("=== USERSERVICE: Rolled back user creation due to messaging error: " + e.getMessage() + " ===");
                } catch (Exception ex) {
                    rollbackNotifications.put(createdUser.getId(), "Critical error - user created but rollback failed: " + ex.getMessage());
                    System.err.println("=== USERSERVICE: Failed to rollback user creation: " + ex.getMessage() + " ===");
                }
            }
            throw new RuntimeException("Failed to create user: " + e.getMessage(), e);
        }

        return createdUser;
    }

    @Override
    public void handleClientCreationRollback(String userId, String reason) {
        System.out.println("=== USERSERVICE: Starting rollback process ===");
        System.out.println("User ID: " + userId);
        System.out.println("Reason: " + reason);

        try {
            User existingUser = userPort.getById(userId);
            if (existingUser == null) {
                System.out.println("=== USERSERVICE: User already deleted or doesn't exist ===");
                rollbackNotifications.put(userId, "User was already deleted or doesn't exist: " + reason);
                return;
            }

            System.out.println("=== USERSERVICE: Found user to delete: " + existingUser.getLogin() + " ===");

            userPort.deleteById(userId);

            rollbackNotifications.put(userId, "User creation rolled back: " + reason);
            System.out.println("=== USERSERVICE: Successfully rolled back user creation for ID: " + userId + " ===");

        } catch (Exception e) {
            rollbackNotifications.put(userId, "Rollback failed: " + e.getMessage());
            System.err.println("=== USERSERVICE: Failed to rollback user creation: " + e.getMessage() + " ===");
        }
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
        return tokenProviderPort.generateToken(user.getLogin(), user.getId(), user.getRole());
    }

    public void invalidateToken(String token) {
        blacklist.add(token);
    }

    public boolean isTokenOnBlackList(String token) {
        return blacklist.contains(token);
    }
}
