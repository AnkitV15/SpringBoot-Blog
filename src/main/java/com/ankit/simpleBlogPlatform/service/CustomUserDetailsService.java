package com.ankit.simpleBlogPlatform.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority; // For roles
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ankit.simpleBlogPlatform.model.User;
import com.ankit.simpleBlogPlatform.repository.UserRepository;

@Service // Mark this as a Spring Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieve user from the database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Build Spring Security's UserDetails object
        // Use SimpleGrantedAuthority for roles. Roles must be prefixed with "ROLE_" (e.g., "ROLE_USER")
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}