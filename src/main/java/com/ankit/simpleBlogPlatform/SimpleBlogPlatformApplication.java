package com.ankit.simpleBlogPlatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing; // Import this

@SpringBootApplication
@EnableJpaAuditing // Add this annotation
public class SimpleBlogPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleBlogPlatformApplication.class, args);
    }
}