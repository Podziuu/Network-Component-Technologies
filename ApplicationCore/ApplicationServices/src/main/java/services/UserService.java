package services;

import dto.*;
import exception.DuplicateUserException;
import exception.UserNotFoundException;
import exception.InvalidCredentialsException;
import infrastructure.UserCommandPort;
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

    public UserDTO addUser(CreateUserDTO user) {
        if (userExists(user.getLogin())) {
            throw new DuplicateUserException("User with login " + user.getLogin() + " already exists");
        }
        User mappedUser = UserMapper.convertToUser(user);
        mappedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = userCommandPort.add(mappedUser);
        return UserMapper.convertToUserDTO(createdUser);
    }

    public UserDTO getUserById(String id) {
        User user = userQueryPort.getById(id);
        if (user == null) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        return UserMapper.convertToUserDTO(user);
    }

//    public UserDTO getUserByLogin(LoginDTO login) {
//        User user = userQueryPort.findByLogin(login.getLogin());
//        if (user == null) {
//            throw new UserNotFoundException("User with login " + login.getLogin() + " not found");
//        }
//        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
//            throw new InvalidCredentialsException("Invalid credentials");
//        }
//        return UserMapper.convertToUserDTO(user);
//    }

    public UserDTO getUserByLogin(String login) {
        User user = userQueryPort.findByLogin(login);
        if (user == null) {
            throw new UserNotFoundException("User with login " + login + " not found");
        }
        return UserMapper.convertToUserDTO(user);
    }

    public List<UserDTO> getUsersByRole(Role role) {
        List<User> users = userQueryPort.findByRole(role);
        if (users.isEmpty()) {
            throw new UserNotFoundException("Users with role " + role + " not found");
        }
        return UserMapper.toDTO(users);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userQueryPort.getAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException("No users found");
        }
        return UserMapper.toDTO(users);
    }

    public List<UserDTO> getUsersByFirstName(String firstName) {
        List<User> users = userQueryPort.findByFirstName(firstName);
        if (users.isEmpty()) {
            throw new UserNotFoundException("Users with first name " + firstName + " not found");
        }
        return UserMapper.toDTO(users);
    }

    public List<UserDTO> getUsersByRoleAndFirstName(Role role, String firstName) {
        List<User> users = userQueryPort.findByRoleAndFirstName(role, firstName);
        if (users.isEmpty()) {
            throw new UserNotFoundException("Users with role " + role + " and first name " + firstName + " not found");
        }
        return UserMapper.toDTO(users);
    }

    public void updateUser(String id, UpdateUserDTO userDTO) {
        if (!findUserById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        userCommandPort.update(id, userDTO.getFirstName(), userDTO.getLastName());
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

    public void changePassword(String username, ChangePasswordDTO dto) {
        User user = userQueryPort.findByLogin(username);
        if (user == null) {
            throw new UserNotFoundException("User with login " + username + " not found");
        }

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        String encodedNewPassword = passwordEncoder.encode(dto.getNewPassword());
        userCommandPort.updatePassword(user.getLogin(), encodedNewPassword);
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
