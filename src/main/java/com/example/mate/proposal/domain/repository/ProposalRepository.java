package com.example.mate.proposal.domain.repository;

import com.example.mate.proposal.domain.Proposal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findByUserId(Long userId);

    Optional<Proposal> findByIdAndUserId(Long proposalId, Long userId);
}
