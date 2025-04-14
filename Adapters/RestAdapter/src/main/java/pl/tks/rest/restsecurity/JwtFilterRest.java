//package pl.tks.rest.restsecurity;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.web.filter.OncePerRequestFilter;
//import pl.tks.model.user.Role;
//import pl.tks.ports.infrastructure.TokenProviderPort;
//import pl.tks.ports.ui.IUserPort;
//
//import java.io.IOException;
//import java.util.Collection;
//import java.util.Collections;
//
//@Component
//public class JwtFilterRest extends OncePerRequestFilter {
//
//    private final TokenProviderPort tokenProviderPort;
//    private final IUserPort userPort;
//
//    @Autowired
//    public JwtFilterRest(TokenProviderPort tokenProviderPort, IUserPort userPort) {
//        this.tokenProviderPort = tokenProviderPort;
//        this.userPort = userPort;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//        String token = getTokenFromRequest(request);
//        if (token != null && tokenProviderPort.validateToken(token)) {
//            if (userPort.isTokenOnBlackList(token)) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("Token has been invalidated");
//                return;
//            }
//            String login = tokenProviderPort.getLogin(token);
//            String roleString = tokenProviderPort.getRole(token);
//            Role role = Role.valueOf(roleString);
//            UserDetails userDetails = userPort.loadUserByUsername(login);
//            Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
//                    null, authorities);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//        filterChain.doFilter(request, response);
//    }
//
//    private String getTokenFromRequest(HttpServletRequest request) {
//        String bearerToken = request.getHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return null;
//    }
//}
