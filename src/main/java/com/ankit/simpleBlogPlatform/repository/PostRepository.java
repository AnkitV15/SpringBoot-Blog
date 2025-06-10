package com.ankit.simpleBlogPlatform.repository;

import com.ankit.simpleBlogPlatform.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    // Custom methods (examples)
    List<Post> findByUser_Id(Long userId); // Find posts by a specific user

    Optional<Post> findByIdAndUser_Id(Long postId, Long userId); // Find a post by ID, ensuring it belongs to a specific user
}