package com.example.mate.client.application;

import com.example.mate.client.domain.Ctag;
import com.example.mate.client.domain.repository.CtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CtagService {

    private final CtagRepository ctagRepository;

    public Ctag findOrCreateTag(String tagName) {
        return ctagRepository.findByName(tagName)
                .orElseGet(() -> ctagRepository.save(new Ctag(tagName)));
    }

    public List<Ctag> findOrCreateTags(List<String> tagNames) {
        List<Ctag> ctags = new ArrayList<>();
        for (String tagName : tagNames) {
            ctags.add(findOrCreateTag(tagName));
        }
        return ctags;
    }
}
