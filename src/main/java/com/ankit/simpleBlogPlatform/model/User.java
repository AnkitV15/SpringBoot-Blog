package com.ankit.simpleBlogPlatform.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Set; // Used for roles, if implementing multiple roles per user

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // Will store the BCrypt encoded password

    // For simplicity, we'll start with a single role as a String.
    // Later, you can expand this to a separate Role entity and a Many-to-Many relationship.
    @Column(nullable = false)
    private String role; // e.g., "ROLE_USER", "ROLE_ADMIN"

    // Optional: Link Posts to Users if you want to track which user authored which post
    // This assumes a one-to-many relationship where one user can have many posts
    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private Set<Post> posts;

    // You might want to add createdAt/updatedAt fields similar to your Post entity
    // private LocalDateTime createdAt;
    // private LocalDateTime updatedAt;
}