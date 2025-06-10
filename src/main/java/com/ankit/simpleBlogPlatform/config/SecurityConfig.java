package com.ankit.simpleBlogPlatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Enables Spring Security's web security support
public class SecurityConfig {

    // --- 1. Password Encoder ---
    // Spring Security requires passwords to be encoded. BCrypt is a strong hashing algorithm.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // --- 2. Security Filter Chain Configuration ---
    // This defines what URLs require authentication and how login/logout behave.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Allow public access to the registration page
                .requestMatchers("/register", "/css/**", "/js/**").permitAll()
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")        // Specify our custom login page URL
                .defaultSuccessUrl("/")     // Redirect to homepage after successful login
                .permitAll()                // Allow everyone to access the login page
            )
            .logout(logout -> logout
                .logoutUrl("/logout")       // The URL to trigger logout (Spring Security handles this)
                .logoutSuccessUrl("/login?logout") // Redirect to login page with a message after logout
                .permitAll()
            );
        return http.build();
    }

    // Note: For now, Spring Security will automatically provide a default in-memory user
    // if you don't define a UserDetailsService. We will override this later to use our database.
}