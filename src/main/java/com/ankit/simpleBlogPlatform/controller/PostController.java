package com.ankit.simpleBlogPlatform.controller;

import com.ankit.simpleBlogPlatform.model.Comment;
import com.ankit.simpleBlogPlatform.model.Post;
import com.ankit.simpleBlogPlatform.model.User;
import com.ankit.simpleBlogPlatform.repository.UserRepository;
import com.ankit.simpleBlogPlatform.service.CommentService;
import com.ankit.simpleBlogPlatform.service.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;
    private final CommentService commentService;

    public PostController(PostService postService, UserRepository userRepository, CommentService commentService) {
        this.postService = postService;
        this.userRepository = userRepository;
        this.commentService = commentService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("tags");
    }

    // Displays the form to create a new post
    @GetMapping("/create")
    public String showCreatePostForm(Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("currentTags", "");
        return "posts/create";
    }

    // Handles the submission of the new post form
    @PostMapping("/create")
    public String createPost(@ModelAttribute Post post,
                             @RequestParam(value = "tags", required = false, defaultValue = "") String tags,
                             RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        postService.createNewPost(post.getTitle(), post.getContent(), username, tags);

        redirectAttributes.addFlashAttribute("successMessage", "Post created successfully!");
        return "redirect:/posts/user";
    }

    // --- Move this method UP ---
    // Displays a list of posts for the logged-in user
    @GetMapping("/user")
    public String showUserPosts(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found!"));

        List<Post> posts = postService.getPostsByUserId(user.getId());

        model.addAttribute("posts", posts);
        return "posts/list";
    }
    // -------------------------

    // Displays a single post AND its comments
    @GetMapping("/{id}") // This should now only match numerical IDs
    public String showPost(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found!"));

        Optional<Post> postOptional = postService.getPostById(id);

        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            model.addAttribute("post", post);

            List<Comment> comments = commentService.getCommentsByPostId(post.getId());
            model.addAttribute("comments", comments);
            model.addAttribute("newComment", new Comment());

            return "posts/view";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Post not found.");
            return "redirect:/";
        }
    }
    // Displays the edit post form
    @GetMapping("/edit/{id}")
    public String showEditPostForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Logged-in user not found!"));

        Optional<Post> post = postService.getPostByIdAndUserId(id, user.getId());
        if (post.isPresent()) {
            model.addAttribute("post", post.get());
            String currentTags = post.get().getTags().stream()
                                    .map(tag -> tag.getName())
                                    .collect(Collectors.joining(", "));
            model.addAttribute("currentTags", currentTags);
            return "posts/edit";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Post not found or you don't have permission to edit it.");
            return "redirect:/posts/user";
        }
    }

 // Handles the submission of the edit post form
    @PostMapping("/edit/{id}")
    public String updatePost(@PathVariable("id") Long id,
                             @ModelAttribute Post post,
                             @RequestParam(value = "tags", required = false, defaultValue = "") String tags,
                             RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // --- START DEBUG LOGS ---
        System.out.println("DEBUG in PostController.updatePost:");
        System.out.println("  Post ID: " + id);
        System.out.println("  Title received: " + post.getTitle());
        System.out.println("  Content received (from @ModelAttribute): " + post.getContent()); // IMPORTANT
        System.out.println("  Tags received (from @RequestParam): " + tags);
        System.out.println("  User: " + username);
        // --- END DEBUG LOGS ---

        postService.updatePost(id, post.getTitle(), post.getContent(), username, tags);
        redirectAttributes.addFlashAttribute("successMessage", "Post updated successfully!");
        return "redirect:/posts/user";
    }

    // Handles deleting a post
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        postService.deletePost(id, username);
        redirectAttributes.addFlashAttribute("successMessage", "Post deleted successfully!");
        return "redirect:/posts/user";
    }

    // Handles the submission of a new comment
    @PostMapping("/{postId}/comments")
    public String addComment(@PathVariable("postId") Long postId,
                             @ModelAttribute("newComment") Comment newComment,
                             RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        try {
            commentService.createComment(postId, newComment.getContent(), username);
            redirectAttributes.addFlashAttribute("successMessage", "Comment added successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/posts/" + postId;
    }
}