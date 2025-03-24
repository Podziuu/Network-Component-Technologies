import dto.*;
import exception.DuplicateUserException;
import exception.InvalidCredentialsException;
import exception.UserNotFoundException;
import infrastructure.UserCommandPort;
import model.user.Client;
import model.user.ClientType;
import model.user.Role;
import model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import query.UserQueryPort;
import security.JwtTokenProvider;
import services.UserService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserCommandPort userCommandPort;

    @Mock
    private UserQueryPort userQueryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private static CreateUserDTO createUser;
    private static UserDTO userDTO;
    private User user2;
    private final String login = "JDoe";
    private final String password = "password";
    private final String firstName = "John";
    private final String lastName = "Doe";
    private final String updatedFirstName = "Jane";
    private final String updatedLastName = "Does";
    private final Role role = Role.CLIENT;
    private final UpdateUserDTO updateUserDTO = new UpdateUserDTO(updatedFirstName, updatedLastName);
    private final String currentPassword = "oldPassword";
    private final String newPassword = "newSecurePassword";
    private final String wrongPassword = "wrongPassword";
    private ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(currentPassword, newPassword);
    private LoginDTO loginDTO = new LoginDTO(login, password);

    @BeforeEach
    void setUp() {
        createUser = new CreateUserDTO(login, password, firstName, lastName, role);
        testUser = new Client("123", login, password, firstName, lastName, ClientType.createNoMembership());
        user2 = new Client("12345", login, password, updatedFirstName, updatedLastName, ClientType.createNoMembership());
    }

    @Test
    void shouldAddUserSuccessfully() {
        when(userQueryPort.userExists(createUser.getLogin())).thenReturn(false);
        when(passwordEncoder.encode(testUser.getPassword())).thenReturn("encodedPassword");
        when(userCommandPort.add(any(User.class))).thenReturn(testUser);

        UserDTO createdUser = userService.addUser(createUser);

        assertNotNull(createdUser);
        verify(userCommandPort).add(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExistsWhileAdding() {
        when(userQueryPort.userExists(createUser.getLogin())).thenReturn(true);

        assertThrows(DuplicateUserException.class, () -> userService.addUser(createUser));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundById() {
        when(userQueryPort.getById("678")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.getUserById("678"));
    }

    @Test
    void shouldGetUserByIdSuccessfully() {
        when(userQueryPort.getById("123")).thenReturn(testUser);

        UserDTO user = userService.getUserById("123");

        assertEquals(testUser.getLogin(), user.getLogin());
        assertEquals(testUser.getFirstName(), user.getFirstName());
        assertEquals(testUser.getLastName(), user.getLastName());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundByLogin() {
        when(userQueryPort.findByLogin(login)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.getUserByLogin(login));
    }

    @Test
    void shouldGetUserByLoginSuccessfully() {
        when(userQueryPort.findByLogin(login)).thenReturn(testUser);

        UserDTO user = userService.getUserByLogin(login);

        assertEquals(testUser.getLogin(), user.getLogin());
        assertEquals(testUser.getFirstName(), user.getFirstName());
        assertEquals(testUser.getLastName(), user.getLastName());
    }

    @Test
    void shouldThrowExceptionWhenNoUsersWithRole() {
        when(userQueryPort.findByRole(role)).thenReturn(Collections.emptyList());

        assertThrows(UserNotFoundException.class, () -> userService.getUsersByRole(role));
    }

    @Test
    void shoulGetUsersByRoleSuccessfully() {
        when(userQueryPort.findByRole(role)).thenReturn(Collections.singletonList(testUser));

        List<UserDTO> users = userService.getUsersByRole(role);

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(testUser.getLogin(), users.get(0).getLogin());
        assertEquals(testUser.getFirstName(), users.get(0).getFirstName());
        assertEquals(testUser.getLastName(), users.get(0).getLastName());
    }

    @Test
    void shouldThrowExceptionWhenNoUsersFound() {
        when(userQueryPort.getAll()).thenReturn(Collections.emptyList());

        assertThrows(UserNotFoundException.class, () -> userService.getAllUsers());
    }

    @Test
    void shouldGetAllUsersSuccessfully() {
        when(userQueryPort.getAll()).thenReturn(Collections.singletonList(testUser));
        List<UserDTO> users = userService.getAllUsers();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(testUser.getLogin(), users.get(0).getLogin());
        assertEquals(testUser.getFirstName(), users.get(0).getFirstName());
        assertEquals(testUser.getLastName(), users.get(0).getLastName());
    }

    @Test
    void shouldThrowExceptionWhenNoUsersFoundByFirstName() {
        when(userQueryPort.findByFirstName("Janusz")).thenReturn(Collections.emptyList());

        assertThrows(UserNotFoundException.class, () -> userService.getUsersByFirstName("Janusz"));
    }

    @Test
    void shouldGetUsersByFirstNameSuccessfully() {
        when(userQueryPort.findByFirstName(firstName)).thenReturn(Collections.singletonList(testUser));
        List<UserDTO> users = userService.getUsersByFirstName(firstName);
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(testUser.getLogin(), users.get(0).getLogin());
        assertEquals(testUser.getFirstName(), users.get(0).getFirstName());
        assertEquals(testUser.getLastName(), users.get(0).getLastName());
    }

    @Test
    void shouldThrowExceptionWhenNoUsersFoundByRoleAndFirstName() {
        when(userQueryPort.findByRoleAndFirstName(role, firstName)).thenReturn(Collections.emptyList());

        assertThrows(UserNotFoundException.class, () -> userService.getUsersByRoleAndFirstName(role, firstName));
    }

    @Test
    void shouldGetUsersByRoleAndFirstNameSuccessfully() {
        when(userQueryPort.findByRoleAndFirstName(role, firstName)).thenReturn(Collections.singletonList(testUser));
        List<UserDTO> users = userService.getUsersByRoleAndFirstName(role, firstName);
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(testUser.getLogin(), users.get(0).getLogin());
        assertEquals(testUser.getFirstName(), users.get(0).getFirstName());
        assertEquals(testUser.getLastName(), users.get(0).getLastName());
    }

    @Test
    void shouldThrowExceptionWhenUpdateUserDoesntExist() {
        when(userQueryPort.getById("456")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.getUserById("456"));
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        when(userQueryPort.getById("123")).thenReturn(testUser);

        userService.updateUser("123", updateUserDTO);

        verify(userCommandPort).update("123", updatedFirstName, updatedLastName);
    }

    @Test
    void shouldThrowExceptionWhenNoUserFoundWhileActivating() {
        when(userQueryPort.getById("456")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.activateUser("456"));
    }

    @Test
    void shouldActivateUserSuccessfully() {
        when(userQueryPort.getById("123")).thenReturn(testUser);

        userService.activateUser("123");

        verify(userCommandPort).activateUser("123");
    }

    @Test
    void shouldThrowExceptionWhenNoUserFoundWhileDeactivating() {
        when(userQueryPort.getById("456")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.deactivateUser("456"));
    }

    @Test
    void shouldDeactivateUserSuccessfully() {
        when(userQueryPort.getById("123")).thenReturn(testUser);

        userService.deactivateUser("123");

        verify(userCommandPort).deactivateUser("123");
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        when(userQueryPort.findByLogin(login)).thenReturn(testUser);
        when(passwordEncoder.matches(currentPassword, testUser.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        userService.changePassword(login, changePasswordDTO);

        verify(userCommandPort).updatePassword(login, "encodedNewPassword");
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundWhileChangingPassword() {
        when(userQueryPort.findByLogin(login)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.changePassword(login, changePasswordDTO));

        verify(userCommandPort, never()).updatePassword(anyString(), anyString());
    }

    @Test
    void shouldThrowExceptionWhenCurrentPasswordIsIncorrect() {
        when(userQueryPort.findByLogin(login)).thenReturn(testUser);
        when(passwordEncoder.matches(currentPassword, testUser.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> userService.changePassword(login, changePasswordDTO));

        verify(userCommandPort, never()).updatePassword(anyString(), anyString());
    }

    @Test
    void shouldLoginSuccessfully() {
        when(userQueryPort.findByLogin(login)).thenReturn(testUser);
        when(passwordEncoder.matches(password, testUser.getPassword())).thenReturn(true);
        when(jwtTokenProvider.generateToken(testUser.getLogin(), testUser.getId(), testUser.getRole())).thenReturn("token123");

        String token = userService.login(loginDTO);

        assertNotNull(token);
        assertEquals("token123", token);
        verify(userQueryPort).findByLogin(login);
        verify(passwordEncoder).matches(password, testUser.getPassword());
        verify(jwtTokenProvider).generateToken(testUser.getLogin(), testUser.getId(), testUser.getRole());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundWhileLogin() {
        when(userQueryPort.findByLogin(login)).thenReturn(null);

        assertThrows(InvalidCredentialsException.class, () -> userService.login(loginDTO));

        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtTokenProvider, never()).generateToken(anyString(), anyString(), any());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsIncorrect() {
        when(userQueryPort.findByLogin(login)).thenReturn(testUser);
        when(passwordEncoder.matches(wrongPassword, testUser.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> userService.login(new LoginDTO(login, wrongPassword)));

        verify(jwtTokenProvider, never()).generateToken(anyString(), anyString(), any());
    }
}
