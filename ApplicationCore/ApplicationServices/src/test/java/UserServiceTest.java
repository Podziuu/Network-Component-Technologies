import pl.tks.ports.infrastructure.UserPort;
import pl.tks.model.user.Client;
import pl.tks.model.user.ClientType;
import pl.tks.model.user.Role;
import pl.tks.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.tks.service.exception.DuplicateUserException;
import pl.tks.service.exception.InvalidCredentialsException;
import pl.tks.service.exception.UserNotFoundException;
import pl.tks.service.services.UserService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserPort userPort;

    @Mock
    private PasswordEncoder passwordEncoder;

//    @Mock
//    private JwtTokenProviderRest jwtTokenProvider;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private final String login = "JDoe";
    private final String password = "password";
    private final String firstName = "John";
    private final Role role = Role.CLIENT;
    private final String currentPassword = "oldPassword";
    private final String newPassword = "newSecurePassword";
    private final String wrongPassword = "wrongPassword";

    private User userToAdd;

    @BeforeEach
    void setUp() {
        userToAdd = new Client();
        userToAdd.setLogin(login);
        userToAdd.setPassword(password);
        userToAdd.setFirstName(firstName);
        userToAdd.setLastName("Doe");
        testUser = new Client("123", login, password, firstName, "Doe", ClientType.createNoMembership());
    }

    @Test
    void shouldAddUserSuccessfully() {
        when(userPort.userExists(userToAdd.getLogin())).thenReturn(false);
        when(passwordEncoder.encode(userToAdd.getPassword())).thenReturn("encodedPassword");
        when(userPort.addUser(any(User.class))).thenReturn(testUser);

        User createdUser = userService.addUser(userToAdd);

        assertNotNull(createdUser);
        verify(userPort).addUser(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        when(userPort.userExists(userToAdd.getLogin())).thenReturn(true);

        assertThrows(DuplicateUserException.class, () -> userService.addUser(userToAdd));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundById() {
        when(userPort.getById("678")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.getUserById("678"));
    }

    @Test
    void shouldGetUserByIdSuccessfully() {
        when(userPort.getById("123")).thenReturn(testUser);

        User user = userService.getUserById("123");

        assertEquals(testUser.getLogin(), user.getLogin());
        assertEquals(testUser.getFirstName(), user.getFirstName());
        assertEquals(testUser.getLastName(), user.getLastName());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundByLogin() {
        when(userPort.findByLogin(login)).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.getUserByLogin(login));
    }

    @Test
    void shouldGetUserByLoginSuccessfully() {
        when(userPort.findByLogin(login)).thenReturn(testUser);

        User user = userService.getUserByLogin(login);

        assertEquals(testUser.getLogin(), user.getLogin());
        assertEquals(testUser.getFirstName(), user.getFirstName());
        assertEquals(testUser.getLastName(), user.getLastName());
    }

    @Test
    void shouldThrowExceptionWhenNoUsersWithRole() {
        when(userPort.findByRole(role)).thenReturn(Collections.emptyList());

        assertThrows(UserNotFoundException.class, () -> userService.getUsersByRole(role));
    }

    @Test
    void shouldGetUsersByRoleSuccessfully() {
        when(userPort.findByRole(role)).thenReturn(Collections.singletonList(testUser));

        List<User> users = userService.getUsersByRole(role);

        assertNotNull(users);
        assertEquals(1, users.size());
    }

    @Test
    void shouldThrowExceptionWhenNoUsersFound() {
        when(userPort.getAll()).thenReturn(Collections.emptyList());

        assertThrows(UserNotFoundException.class, () -> userService.getAllUsers());
    }

    @Test
    void shouldGetAllUsersSuccessfully() {
        when(userPort.getAll()).thenReturn(Collections.singletonList(testUser));

        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentUser() {
        when(userPort.getById("456")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> userService.updateUser("456", "Jane", "Doe"));
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        when(userPort.getById("123")).thenReturn(testUser);

        userService.updateUser("123", "Jane", "Does");

        verify(userPort).updateUser("123", "Jane", "Does");
    }

    @Test
    void shouldActivateUserSuccessfully() {
        when(userPort.getById("123")).thenReturn(testUser);

        userService.activateUser("123");

        verify(userPort).activateUser("123");
    }

    @Test
    void shouldDeactivateUserSuccessfully() {
        when(userPort.getById("123")).thenReturn(testUser);

        userService.deactivateUser("123");

        verify(userPort).deactivateUser("123");
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        when(userPort.findByLogin(login)).thenReturn(testUser);
        when(passwordEncoder.matches(currentPassword, testUser.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        userService.changePassword(login, currentPassword, newPassword);

        verify(userPort).updatePassword(login, "encodedNewPassword");
    }

    @Test
    void shouldThrowExceptionWhenCurrentPasswordIsIncorrect() {
        when(userPort.findByLogin(login)).thenReturn(testUser);
        when(passwordEncoder.matches(currentPassword, testUser.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> userService.changePassword(login, currentPassword, newPassword));

        verify(userPort, never()).updatePassword(anyString(), anyString());
    }

//    @Test
//    void shouldLoginSuccessfully() {
//        when(userPort.findByLogin(login)).thenReturn(testUser);
//        when(passwordEncoder.matches(password, testUser.getPassword())).thenReturn(true);
//        when(jwtTokenProvider.generateToken(login, testUser.getId(), testUser.getRole())).thenReturn("token123");
//
//        String token = userService.login(login, password);
//
//        assertNotNull(token);
//        assertEquals("token123", token);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenInvalidLogin() {
//        when(userPort.findByLogin(login)).thenReturn(null);
//
//        assertThrows(InvalidCredentialsException.class, () -> userService.login(login, password));
//
//        verify(jwtTokenProvider, never()).generateToken(anyString(), anyString(), any());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenIncorrectPassword() {
//        when(userPort.findByLogin(login)).thenReturn(testUser);
//        when(passwordEncoder.matches(wrongPassword, testUser.getPassword())).thenReturn(false);
//
//        assertThrows(InvalidCredentialsException.class, () -> userService.login(login, wrongPassword));
//
//        verify(jwtTokenProvider, never()).generateToken(anyString(), anyString(), any());
//    }
}
