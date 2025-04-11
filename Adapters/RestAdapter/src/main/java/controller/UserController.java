package controller;

import dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import model.user.Role;
import model.user.User;
import model.user.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import security.JwsProvider;
//import security.JwtTokenProvider;
import ui.IUserPort;
import mapper.UserMapper;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final IUserPort userPort;
//    private final JwtTokenProvider jwtTokenProvider;
    private final JwsProvider jwsProvider;
    private final UserMapper userMapper;

    public UserController(IUserPort userPort /*, JwtTokenProvider jwtTokenProvider*/, JwsProvider jwsProvider, UserMapper userMapper) {
        this.userPort = userPort;
//        this.jwtTokenProvider = jwtTokenProvider;
        this.jwsProvider = jwsProvider;
        this.userMapper = userMapper;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getAllUsers(
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) String firstName) {
        List<User> users;

        if (role != null && firstName != null) {
            users = userPort.getUsersByRoleAndFirstName(role, firstName);
        } else if (firstName != null) {
            users = userPort.getUsersByFirstName(firstName);
        } else if (role == null) {
            users = userPort.getAllUsers();
        } else {
            users = userPort.getUsersByRole(role);
        }

        return userMapper.toDTO(users);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO addUser(@RequestBody @Valid CreateUserDTO userDTO) {
        User user = userMapper.convertToUser(userDTO);
        User createdUser = userPort.addUser(user);
        return userMapper.convertToDTO(createdUser);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        User user = userPort.getUserById(id);
        UserDTO userDTO = userMapper.convertToDTO(user);
        String jws = jwsProvider.generateJws(id);
//        boolean isValid = jwtTokenProvider.validateToken(jws);
        return ResponseEntity.ok()
                .header("ETag", jws)
                .body(userDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody @Valid UpdateUserDTO userDTO, @RequestHeader("ETag") String jws) {
        boolean isValid = jwsProvider.validateJws(jws, id);
        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        userPort.updateUser(id, userDTO.getFirstName(), userDTO.getLastName());

        return ResponseEntity.noContent().build();
    }


    @PutMapping("/activate/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activateUser(@PathVariable String id) {
        userPort.activateUser(id);
    }

    @PutMapping("/deactivate/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateUser(@PathVariable String id) {
        userPort.deactivateUser(id);
    }

//    @PutMapping("/me/changePassword")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void changePassword(@RequestBody @Valid ChangePasswordDTO dto) {
//        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder
//                .getContext()
//                .getAuthentication()
//                .getPrincipal();
//        String username = userPrincipal.getUsername();
//        userPort.changePassword(username, dto);
//    }

//    @PostMapping("/login")
//    @ResponseStatus(HttpStatus.OK)
//    public TokenDTO login(@RequestBody @Valid LoginDTO dto) {
//        String token = userPort.login(dto);
//        return new TokenDTO(token);
//    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
//        if (token != null) {
//            userPort.invalidateToken(token);
//        }
    }

//    @GetMapping("/me")
//    @ResponseStatus(HttpStatus.OK)
//    public UserDTO getCurrentUser(HttpServletRequest request) {
//        String token = getTokenFromRequest(request);
//        if (token == null || !jwtTokenProvider.validateToken(token)) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or missing token");
//        }
//        String username = jwtTokenProvider.getLogin(token);
//        User user = userPort.getUserByLogin(username);
//
//        return userMapper.convertToDTO(user);
//    }

}
