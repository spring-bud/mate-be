package com.example.mate.client.domain.repository;

import com.example.mate.client.domain.Client;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("SELECT c FROM Client c " +
            "LEFT JOIN FETCH c.user u " +
            "LEFT JOIN FETCH c.clientTags ct " +
            "LEFT JOIN FETCH ct.ctag " +
            "WHERE c.id = :clientId " +
            "AND u.status != 'DELETED'")
    Optional<Client> findByIdWithUserAndTags(@Param("clientId") Long clientId);
}
