package com.ankit.simpleBlogPlatform.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments") // Optional: specifies table name if different from class name
@EntityListeners(AuditingEntityListener.class) // Enable auditing for auto-timestamping
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob // For potentially long comment content
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY) // Many comments can belong to one post
    @JoinColumn(name = "post_id", nullable = false) // Foreign key to the Post table
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY) // Many comments can be made by one user
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to the User table
    private User user; // The user who made this comment

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // --- Constructors ---
    public Comment() {
    }

    public Comment(String content, Post post, User user) {
        this.content = content;
        this.post = post;
        this.user = user;
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // --- Convenience Method for Display (Optional) ---
    @Transient // This field will not be mapped to the database
    public String getAuthorName() {
        return (user != null) ? user.getUsername() : "Anonymous";
    }
}