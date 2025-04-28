package com.example.mate.client.domain.repository;

import com.example.mate.client.domain.Ctag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CtagRepository extends JpaRepository<Ctag, Long> {

    Optional<Ctag> findByName(String name);
}
