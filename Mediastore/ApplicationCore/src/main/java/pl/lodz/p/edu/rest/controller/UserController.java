package pl.lodz.p.edu.rest.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.lodz.p.edu.rest.dto.*;
import pl.lodz.p.edu.rest.model.user.Role;
import pl.lodz.p.edu.rest.model.user.UserPrincipal;
import pl.lodz.p.edu.rest.security.JwsProvider;
import pl.lodz.p.edu.rest.security.JwtTokenProvider;
import pl.lodz.p.edu.rest.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwsProvider jwsProvider;

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider, JwsProvider jwsProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwsProvider = jwsProvider;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getAllUsers(
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) String firstName) {
        if (role != null && firstName != null) {
            return userService.getUsersByRoleAndFirstName(role, firstName);
        } else if (firstName != null) {
            return userService.getUsersByFirstName(firstName);
        } else if (role == null) {
            return userService.getAllUsers();
        } else {
            return userService.getUsersByRole(role);
        }
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO addUser(@RequestBody @Valid CreateUserDTO userDTO) {
        return userService.addUser(userDTO);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> getUser(@PathVariable String id) {
        UserDTO user = userService.getUserById(id);
        String jws = jwsProvider.generateJws(id);

        boolean isValid = jwtTokenProvider.validateToken(jws);
        System.out.println(isValid);
        System.out.println(jws);

        return ResponseEntity.ok()
                .header("ETag", jws)
                .body(user);
}

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody @Valid UpdateUserDTO userDTO, @RequestHeader("ETag") String jws) {
        boolean isValid = jwsProvider.validateJws(jws, id);
        if (!isValid) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        userService.updateUser(id, userDTO);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/activate/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activateUser(@PathVariable String id) {
        userService.activateUser(id);
    }

    @PutMapping("/deactivate/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateUser(@PathVariable String id) {
        userService.deactivateUser(id);
    }

    @PutMapping("/me/changePassword")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@RequestBody @Valid ChangePasswordDTO dto) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        String username = userPrincipal.getUsername();
        userService.changePassword(username, dto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenDTO login(@RequestBody @Valid LoginDTO dto) {
        String token = userService.login(dto);
        return new TokenDTO(token);
    }

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
        if (token != null) {
            userService.invalidateToken(token);
        }
    }
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getCurrentUser(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or missing token");
        }
        String username = jwtTokenProvider.getLogin(token);
        return userService.getUserByLogin(username);
    }
}
