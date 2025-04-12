package pl.lodz.p.edu.restadapters.restsecurity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import ui.IUserPort;

import java.io.IOException;

@Service
public class JwtFilterRest extends OncePerRequestFilter {

    private final JwtTokenProviderRest jwtTokenProvider;
    private final IUserPort userPort;

    @Autowired
    public JwtFilterRest(JwtTokenProviderRest jwtTokenProvider, IUserPort userPort) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userPort = userPort;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Logic for handling the JWT token here
        String token = getTokenFromRequest(request);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            String login = jwtTokenProvider.getLogin(token);
            String role = jwtTokenProvider.getRole(token);

            // You can set the authentication context here if needed
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}