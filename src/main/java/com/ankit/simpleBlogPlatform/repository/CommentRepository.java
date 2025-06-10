package com.ankit.simpleBlogPlatform.repository;

import com.ankit.simpleBlogPlatform.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Custom method to find all comments belonging to a specific post, ordered by creation date
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);

    // You might also want to add methods like:
    // Optional<Comment> findByIdAndUserId(Long commentId, Long userId);
    // void deleteByIdAndUserId(Long commentId, Long userId);
}