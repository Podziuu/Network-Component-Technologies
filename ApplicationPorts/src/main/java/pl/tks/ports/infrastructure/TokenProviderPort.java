package pl.tks.ports.infrastructure;

import pl.tks.model.user.Role;

public interface TokenProviderPort {
    String generateToken(String login, String userId, Role role);
    String getLogin(String token);
    String getRole(String token);
    boolean validateToken(String token);
}