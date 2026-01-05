package com.example.orderinventory.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${APP_SECURITY_USER:admin}")
    private String appUser;

    @Value("${APP_SECURITY_PASSWORD:password}")
    private String appPassword;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService users(PasswordEncoder passwordEncoder) {
        // Demo in-memory user; credentials configurable via environment variables.
        var user = User.withUsername(appUser)
                .password(passwordEncoder.encode(appPassword))
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // stateless API
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // CORS is configured elsewhere (WebConfig)
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Public reads
                .requestMatchers(HttpMethod.GET, "/orders/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/inventory/**").permitAll()
                .requestMatchers("/actuator/health", "/actuator/info", "/actuator/prometheus").permitAll()
                // Write operations require authentication
                .requestMatchers(HttpMethod.POST, "/orders/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/orders/*/confirm").authenticated()
                .requestMatchers(HttpMethod.POST, "/orders/*/cancel").authenticated()
                // any other request authenticated
                .anyRequest().authenticated()
            )
            // HTTP Basic auth
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}