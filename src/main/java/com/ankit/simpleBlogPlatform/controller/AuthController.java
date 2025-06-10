package com.ankit.simpleBlogPlatform.controller;

import com.ankit.simpleBlogPlatform.model.User;
import com.ankit.simpleBlogPlatform.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Displays the registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User()); // Add a new User object to bind form data
        return "register"; // Returns the name of the Thymeleaf template (register.html)
    }

    // Handles the registration form submission
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            RedirectAttributes redirectAttributes) { // Used for flash attributes (messages after redirect)

        try {
            authService.registerNewUser(username, email, password);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login.");
            return "redirect:/login"; // Redirect to login page after successful registration
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register"; // Redirect back to registration with error message
        }
    }

    // Displays the custom login page (Spring Security handles the actual POST /login processing)
    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password.");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }
        return "login"; // Returns the name of the Thymeleaf template (login.html)
    }
}