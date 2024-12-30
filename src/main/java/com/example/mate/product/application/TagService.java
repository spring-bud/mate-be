package com.example.mate.product.application;

import com.example.mate.product.domain.Tag;
import com.example.mate.product.domain.repository.TagRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag findOrCreateTag(String tagName) {
        return tagRepository.findByName(tagName)
                .orElseGet(() -> tagRepository.save(new Tag(tagName)));
    }

    public List<Tag> findOrCreateTags(List<String> tagNames) {
        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            tags.add(findOrCreateTag(tagName));
        }
        return tags;
    }
}
