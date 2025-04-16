package com.example.mate.user.application;

import com.example.mate.user.domain.Stack;
import com.example.mate.user.domain.repository.StackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StackService {

    private final StackRepository stackRepository;

    public Stack findOrCreateStack(String stackName) {
        return stackRepository.findByName(stackName)
                .orElseGet(() -> stackRepository.save(new Stack(stackName)));
    }

    public List<Stack> findOrCreateStacks(List<String> stackNames) {
        List<Stack> stacks = new ArrayList<>();
        for (String stackName : stackNames) {
            stacks.add(findOrCreateStack(stackName));
        }
        return stacks;
    }
}
