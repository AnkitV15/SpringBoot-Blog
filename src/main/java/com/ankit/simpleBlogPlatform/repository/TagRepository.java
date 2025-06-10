package com.ankit.simpleBlogPlatform.repository;

import com.ankit.simpleBlogPlatform.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Import Optional

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    // Custom method to find a tag by its name (case-insensitive)
    Optional<Tag> findByNameIgnoreCase(String name);
}