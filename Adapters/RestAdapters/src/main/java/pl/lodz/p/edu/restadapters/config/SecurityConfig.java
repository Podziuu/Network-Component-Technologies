package pl.lodz.p.edu.restadapters.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.lodz.p.edu.restadapters.restsecurity.JwtFilterRest;
import pl.lodz.p.edu.restadapters.restsecurity.JwtTokenProviderRest;
import ui.IUserPort;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtFilterRest jwtFilter;

    public SecurityConfig(JwtTokenProviderRest jwtTokenProvider, IUserPort userPort) {
        this.jwtFilter = new JwtFilterRest(jwtTokenProvider, userPort);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/activate/**", "/api/users/deactivate/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.POST, "api/users", "/api/users/login", "/api/users/logout").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
