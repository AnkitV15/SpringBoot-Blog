package com.ankit.simpleBlogPlatform.controller;

import com.ankit.simpleBlogPlatform.model.Post;
import com.ankit.simpleBlogPlatform.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final PostService postService;

    // Inject PostService into HomeController
    public HomeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/") // This maps to the root URL (e.g., http://localhost:8080/)
    public String home(Model model) {
        // Fetch all posts, ordered by the latest first
        // You'll add this method to your PostService next
        List<Post> latestPosts = postService.findAllOrderedByCreatedAtDesc();

        // Add the list of posts to the Model, which Thymeleaf can then access
        model.addAttribute("posts", latestPosts);

        // Return the name of the Thymeleaf template for the home page
        return "index"; // This will look for src/main/resources/templates/index.html
    }
}