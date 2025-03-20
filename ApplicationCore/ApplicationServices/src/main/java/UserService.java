import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.lodz.p.edu.rest.dto.*;
import pl.lodz.p.edu.rest.exception.DuplicateUserException;
import pl.lodz.p.edu.rest.exception.InvalidCredentialsException;
import pl.lodz.p.edu.rest.exception.UserNotFoundException;
import pl.lodz.p.edu.rest.mapper.UserMapper;
import pl.lodz.p.edu.rest.model.user.*;
import pl.lodz.p.edu.rest.repository.UserRepository;
import pl.lodz.p.edu.rest.security.JwtTokenProvider;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper = new UserMapper();
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public UserDTO addUser(CreateUserDTO user) {
        if (userExists(user.getLogin())) {
            throw new DuplicateUserException("User with login " + user.getLogin() + " already exists");
        }
        User createdUser = userMapper.convertToUser(user);
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
        ObjectId id = userRepository.save(createdUser);
        createdUser.setId(id);
        return userMapper.convertToUserDTO(createdUser);
    }

    public UserDTO getUserById(String id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        return userMapper.convertToUserDTO(user);
    }

    public UserDTO getUserByLogin(LoginDTO login) {
        User user = userRepository.findByLogin(login.getLogin());
        if (user == null) {
            throw new UserNotFoundException("User with login " + login.getLogin() + " not found");
        }
        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return userMapper.convertToUserDTO(user);
    }

    public UserDTO getUserByLogin(String login) {
        User user = userRepository.findByLogin(login);
        if (user == null) {
            throw new UserNotFoundException("User with login " + login + " not found");
        }
        return userMapper.convertToUserDTO(user);
    }

    public List<UserDTO> getUsersByRole(Role role) {
        List<User> users = userRepository.findByRole(role);
        if (users.isEmpty()) {
            throw new UserNotFoundException("Users with role " + role + " not found");
        }
        return userMapper.toDTO(users);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new UserNotFoundException("No users found");
        }
        return userMapper.toDTO(users);
    }

    public List<UserDTO> getUsersByFirstName(String firstName) {
        List<User> users = userRepository.findByFirstName(firstName);
        if (users.isEmpty()) {
            throw new UserNotFoundException("Users with first name " + firstName + " not found");
        }
        return userMapper.toDTO(users);
    }

    public List<UserDTO> getUsersByRoleAndFirstName(Role role, String firstName) {
        List<User> users = userRepository.findByRoleAndFirstName(role, firstName);
        if (users.isEmpty()) {
            throw new UserNotFoundException("Users with role " + role + " and first name " + firstName + " not found");
        }
        return userMapper.toDTO(users);
    }

    public void updateUser(String id, UpdateUserDTO userDTO) {
        if (!findUserById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        UpdateResult result = userRepository.update(id, userDTO.getFirstName(), userDTO.getLastName());
        if (result.getModifiedCount() == 0) {
            throw new RuntimeException("User with id " + id + " not updated");
        }
    }

    public void activateUser(String id) {
        if (!findUserById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        UpdateResult result = userRepository.activateUser(id);
        if (result.getModifiedCount() == 0) {
            throw new RuntimeException("User with id " + id + " not updated");
        }
    }

    public void deactivateUser(String id) {
        if (!findUserById(id)) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        UpdateResult result = userRepository.deactivateUser(id);
        if (result.getModifiedCount() == 0) {
            throw new RuntimeException("User with id " + id + " not updated");
        }
    }

    private boolean userExists(String login) {
        return userRepository.userExists(login);
    }

    private boolean findUserById(String id) {
        return userRepository.findById(id) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username);
        if (user == null) {
            throw new UserNotFoundException("User with login " + username + " not found");
        }
        return new UserPrincipal(user);
    }

    public void changePassword(String username, ChangePasswordDTO dto) {
        User user = userRepository.findByLogin(username);
        if (user == null) {
            throw new UserNotFoundException("User with login " + username + " not found");
        }

        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        String encodedNewPassword = passwordEncoder.encode(dto.getNewPassword());
        UpdateResult result = userRepository.updatePassword(user.getLogin(), encodedNewPassword);
        if (result.getModifiedCount() == 0) {
            throw new RuntimeException("Failed to update password for user with login " + username);
        }
    }

    public String login(LoginDTO loginDTO) {
        User user = userRepository.findByLogin(loginDTO.getLogin());
        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid login or password");
        }
        return jwtTokenProvider.generateToken(user.getLogin(), user.getId().toHexString(), user.getRole());
    }

    public void invalidateToken(String token) {
        blacklist.add(token);
    }

    public boolean isTokenOnBlackList(String token) {
        return blacklist.contains(token);
    }
}
