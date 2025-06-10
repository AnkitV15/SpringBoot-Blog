package com.ankit.simpleBlogPlatform.service;

import com.ankit.simpleBlogPlatform.model.User;
import com.ankit.simpleBlogPlatform.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Inject the password encoder

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUser(String username, String email, String password) {
        // Basic validation (you'll want more robust validation later)
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists!"); // Or a custom exception
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered!"); // Or a custom exception
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password)); // Encode password before saving
        newUser.setRole("ROLE_USER"); // Assign default role for new users

        return userRepository.save(newUser);
    }
}