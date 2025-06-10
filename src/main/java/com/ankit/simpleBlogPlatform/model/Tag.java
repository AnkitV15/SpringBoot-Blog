package com.ankit.simpleBlogPlatform.model;

import jakarta.persistence.*;
import java.util.HashSet; // Import HashSet
import java.util.Set;     // Import Set

@Entity
@Table(name = "tags") // Optional: defines table name if different from class name
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // Tag names should be unique
    private String name;

    // Many-to-many relationship with Post
    // mappedBy indicates that the Post entity owns the relationship (it's the inverse side)
    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts = new HashSet<>(); // Initialize to prevent NullPointerException

    // --- Constructors ---
    public Tag() {}

    public Tag(String name) {
        this.name = name;
    }

    // --- Getters and Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    // --- hashCode and equals (important for Set collections) ---
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.toLowerCase().hashCode()); // Case-insensitive hash
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Tag other = (Tag) obj;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equalsIgnoreCase(other.name)) return false; // Case-insensitive comparison
        return true;
    }
}