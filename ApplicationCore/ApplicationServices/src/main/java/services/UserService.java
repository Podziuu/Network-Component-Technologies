package services;

import com.mongodb.client.result.UpdateResult;
import command.UserCommandPort;
import dto.*;
import exception.DuplicateUserException;
import exception.UserNotFoundException;
import exception.InvalidCredentialsException;
import mapper.UserMapper;
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
//    private final UserRepository userRepository;
    private final UserQueryPort userQueryPort;
    private final UserCommandPort userCommandPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();
    private final UserMapper userMapper = new UserMapper();

    @Autowired
    public UserService(UserQueryPort userQueryPort, UserCommandPort userCommandPort, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userQueryPort = userQueryPort;
        this.userCommandPort = userCommandPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public UserDTO addUser(CreateUserDTO user) {
        if (userExists(user.getLogin())) {
            throw new DuplicateUserException("User with login " + user.getLogin() + " already exists");
        }
        User createdUser = userMapper.convertToUser(user);
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userCommandPort.add(createdUser);
//        createdUser.setId(id);
        return userMapper.convertToUserDTO(createdUser);
    }

    public UserDTO getUserById(String id) {
        User user = userQueryPort.getById(id);
        if (user == null) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        return userMapper.convertToUserDTO(user);
    }

    public UserDTO getUserByLogin(LoginDTO login) {
        User user = userQueryPort.findByLogin(login.getLogin());
        if (user == null) {
            throw new UserNotFoundException("User with login " + login.getLogin() + " not found");
        }
        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return userMapper.convertToUserDTO(user);
    }

    public UserDTO getUserByLogin(String login) {
        User user = userQueryPort.findByLogin(login);
        if (user == null) {
            throw new UserNotFoundException("User with login " + login + " not found");
        }
        return userMapper.convertToUserDTO(user);
    }

    public List<UserDTO> getUsersByRole(Role role) {
        List<User> users = userQueryPort.findByRole(role);
        if (users.isEmpty()) {
            throw new UserNotFoundException("Users with role " + role + " not found");
        }
        return userMapper.toDTO(users);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userQueryPort.getAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException("No users found");
        }
        return userMapper.toDTO(users);
    }

    public List<UserDTO> getUsersByFirstName(String firstName) {
        List<User> users = userQueryPort.findByFirstName(firstName);
        if (users.isEmpty()) {
            throw new UserNotFoundException("Users with first name " + firstName + " not found");
        }
        return userMapper.toDTO(users);
    }

    public List<UserDTO> getUsersByRoleAndFirstName(Role role, String firstName) {
        List<User> users = userQueryPort.findByRoleAndFirstName(role, firstName);
        if (users.isEmpty()) {
            throw new UserNotFoundException("Users with role " + role + " and first name " + firstName + " not found");
        }
        return userMapper.toDTO(users);
    }

    public void updateUser(String id, UpdateUserDTO userDTO) {
        if (!findUserById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        // TODO: przemyslec o tym bo updateresult to klasa z mongo
//        UpdateResult result = userCommandPort.update(id, userDTO.getFirstName(), userDTO.getLastName());
//        if (result.getModifiedCount() == 0) {
//            throw new RuntimeException("User with id " + id + " not updated");
//        }
    }

    public void activateUser(String id) {
        if (!findUserById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        // TODO: przemyslec o tym bo updateresult to klasa z mongo
//        UpdateResult result = userCommandPort.activateUser(id);
//        if (result.getModifiedCount() == 0) {
//            throw new RuntimeException("User with id " + id + " not updated");
//        }
    }

    public void deactivateUser(String id) {
        if (!findUserById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        // TODO: przemyslec o tym bo updateresult to klasa z mongo
//        UpdateResult result = userCommandPort.deactivateUser(id);
//        if (result.getModifiedCount() == 0) {
//            throw new RuntimeException("User with id " + id + " not updated");
//        }
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

    public void changePassword(String username, ChangePasswordDTO dto) {
        User user = userQueryPort.findByLogin(username);
        if (user == null) {
            throw new UserNotFoundException("User with login " + username + " not found");
        }

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        String encodedNewPassword = passwordEncoder.encode(dto.getNewPassword());
        // TODO: przemyslec o tym bo updateresult to klasa z mongo
//        UpdateResult result = userRepository.updatePassword(user.getLogin(), encodedNewPassword);
//        if (result.getModifiedCount() == 0) {
//            throw new RuntimeException("Failed to update password for user with login " + username);
//        }
    }

    public String login(LoginDTO loginDTO) {
        User user = userQueryPort.findByLogin(loginDTO.getLogin());
        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
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
