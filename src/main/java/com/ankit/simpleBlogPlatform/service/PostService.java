package com.ankit.simpleBlogPlatform.service;

import com.ankit.simpleBlogPlatform.model.Post;
import com.ankit.simpleBlogPlatform.model.Tag; // Import Tag
import com.ankit.simpleBlogPlatform.model.User;
import com.ankit.simpleBlogPlatform.repository.PostRepository;
import com.ankit.simpleBlogPlatform.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import for transactional operations

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set; // Import Set

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagService tagService; // Inject TagService here!

    public PostService(PostRepository postRepository, UserRepository userRepository, TagService tagService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.tagService = tagService; // Initialize it
    }

    public List<Post> findAllOrderedByCreatedAtDesc() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Optional<Post> getPostById(Long postId) {
        return postRepository.findById(postId);
    }

    @Transactional // Ensure the entire operation is a single transaction
    public Post createNewPost(String title, String content, String username, String tagNamesString) { // Add tagNamesString
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Post newPost = new Post();
        newPost.setTitle(title);
        newPost.setContent(content);
        newPost.setUser(user);
        newPost.setCreatedAt(LocalDateTime.now());

        // Process and set tags
        Set<Tag> tags = tagService.getTagsFromNames(tagNamesString);
        newPost.setTags(tags); // Set the tags on the post

        return postRepository.save(newPost);
    }

    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findByUser_Id(userId);
    }

    public Optional<Post> getPostByIdAndUserId(Long postId, Long userId) {
        return postRepository.findByIdAndUser_Id(postId, userId);
    }

    @Transactional
    public Post updatePost(Long postId, String title, String content, String username, String tagNamesString) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Post post = postRepository.findByIdAndUser_Id(postId, user.getId())
                .orElseThrow(() -> new RuntimeException("Post not found or you don't have permission to edit it."));

        // --- START DEBUG LOGS ---
        System.out.println("DEBUG in PostService.updatePost:");
        System.out.println("  Retrieving post with ID: " + postId);
        System.out.println("  Post content before update (from DB): " + post.getContent());
        System.out.println("  New title received (from controller): " + title);
        System.out.println("  New content received (from controller): " + content); // IMPORTANT
        System.out.println("  New tags received (from controller): " + tagNamesString);
        // --- END DEBUG LOGS ---

        post.setTitle(title);
        post.setContent(content); // This is where the update happens on the entity

        // Process and update tags
        Set<Tag> updatedTags = tagService.getTagsFromNames(tagNamesString);
        post.getTags().clear();
        post.getTags().addAll(updatedTags);

        // --- DEBUG LOG BEFORE SAVE ---
        System.out.println("DEBUG in PostService.updatePost: Post content just before saving: " + post.getContent());
        // --- END DEBUG LOG ---

        return postRepository.save(post);
    }

     public void deletePost(Long postId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        Post post = postRepository.findByIdAndUser_Id(postId, user.getId())
                .orElseThrow(() -> new RuntimeException("Post not found or you don't have permission to delete it."));

        postRepository.delete(post);
    }
}