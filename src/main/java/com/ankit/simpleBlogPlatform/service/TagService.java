package com.ankit.simpleBlogPlatform.service;

import com.ankit.simpleBlogPlatform.model.Tag;
import com.ankit.simpleBlogPlatform.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    // Find a tag by name (case-insensitive)
    public Optional<Tag> findTagByName(String tagName) {
        return tagRepository.findByNameIgnoreCase(tagName);
    }

    // Get or create a tag
    // If the tag already exists (case-insensitive), return it. Otherwise, create and save a new one.
    @Transactional
    public Tag findOrCreateTag(String tagName) {
        return findTagByName(tagName)
                .orElseGet(() -> tagRepository.save(new Tag(tagName.toLowerCase()))); // Save new tag with lowercase name
    }

    // Get a set of Tag objects from a comma-separated string of tag names
    @Transactional
    public Set<Tag> getTagsFromNames(String tagNamesString) {
        Set<Tag> tags = new HashSet<>();
        if (tagNamesString != null && !tagNamesString.trim().isEmpty()) {
            String[] names = tagNamesString.split(",");
            for (String name : names) {
                String trimmedName = name.trim();
                if (!trimmedName.isEmpty()) {
                    tags.add(findOrCreateTag(trimmedName));
                }
            }
        }
        return tags;
    }

    // Get all existing tags (optional, for displaying a list of popular tags, etc.)
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }
}