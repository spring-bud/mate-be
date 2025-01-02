package com.example.mate.user.domain.repository;

import com.example.mate.user.domain.Stack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StackRepository extends JpaRepository<Stack, Long> {
    Optional<Stack> findByName(String name);
}
