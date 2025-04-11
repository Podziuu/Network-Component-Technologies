//package config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//import security.JwtFilter;
//import security.JwtTokenProvider;
//import ui.IItemPort;
//import ui.IUserPort;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//    private final JwtFilter jwtFilter;
//
//    @Autowired
//    public SecurityConfig(IUserPort userPort, JwtTokenProvider jwtTokenProvider) {
//        this.jwtFilter = new JwtFilter(jwtTokenProvider, userPort);
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/users/activate/**", "/api/users/deactivate/**").hasAnyRole("ADMIN", "MANAGER")
//                        .requestMatchers(HttpMethod.POST, "/api/users", "/api/users/login", "api/users/logout").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//}
