package com.ankit.simpleBlogPlatform.repository;

import com.ankit.simpleBlogPlatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Custom method to find a user by username (Spring Data JPA will implement this)
    Optional<User> findByUsername(String username);

    // Optional: find by email if you want to allow login by email
    Optional<User> findByEmail(String email);
}