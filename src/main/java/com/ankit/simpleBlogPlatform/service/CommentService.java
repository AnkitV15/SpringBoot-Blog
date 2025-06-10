package com.ankit.simpleBlogPlatform.service;

import com.ankit.simpleBlogPlatform.model.Comment;
import com.ankit.simpleBlogPlatform.model.Post;
import com.ankit.simpleBlogPlatform.model.User;
import com.ankit.simpleBlogPlatform.repository.CommentRepository;
import com.ankit.simpleBlogPlatform.repository.PostRepository; // Needed to find the Post
import com.ankit.simpleBlogPlatform.repository.UserRepository; // Needed to find the User
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import for transactional operations

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository; // Inject PostRepository
    private final UserRepository userRepository; // Inject UserRepository

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // Method to create a new comment
    @Transactional // Ensures the entire operation is a single transaction
    public Comment createComment(Long postId, String commentContent, String username) {
        // 1. Find the Post to which the comment belongs
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));

        // 2. Find the User who is making the comment
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // 3. Create a new Comment object
        Comment comment = new Comment();
        comment.setContent(commentContent);
        comment.setPost(post); // Link the comment to the post
        comment.setUser(user); // Link the comment to the user

        // 4. Save the comment to the database
        return commentRepository.save(comment);
    }

    // Method to get all comments for a specific post
    public List<Comment> getCommentsByPostId(Long postId) {
        // This uses the custom method we defined in CommentRepository
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId);
    }

    // Optional: Add methods for deleting/editing comments later if needed
    // public void deleteComment(Long commentId, String username) { ... }
    // public Comment updateComment(Long commentId, String newContent, String username) { ... }
}