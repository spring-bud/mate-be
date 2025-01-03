package com.example.mate.user.application;

import com.example.mate.user.domain.Stack;
import com.example.mate.user.domain.repository.StackRepository;
import com.example.mate.user.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.mate.user.exception.UserExceptionType.DUPLICATE_STACK;

@Service
@RequiredArgsConstructor
public class StackService {

    private final StackRepository stackRepository;
    
    public Stack findOrCreateStack(String stackName) {
        stackRepository.findByName(stackName)
                .ifPresent(stack -> {
                    throw new UserException(DUPLICATE_STACK);
                });
        return stackRepository.save(new Stack(stackName));
    }

    public List<Stack> findOrCreateStacks(List<String> stackNames) {
        List<Stack> stacks = new ArrayList<>();
        for (String stackName : stackNames) {
            stacks.add(findOrCreateStack(stackName));
        }
        return stacks;
    }
}
