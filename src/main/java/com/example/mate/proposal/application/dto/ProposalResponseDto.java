package com.example.mate.proposal.application.dto;

import com.example.mate.proposal.domain.Proposal;

import java.util.List;
import java.util.stream.Collectors;


public record ProposalResponseDto(
        String title,
        String description
) {

    public static ProposalResponseDto of(Proposal proposal) {
        return new ProposalResponseDto(
                proposal.getTitle(),
                proposal.getDescription()
        );
    }

    public static List<ProposalResponseDto> of(List<Proposal> proposals) {
        return proposals.stream()
                .map(ProposalResponseDto::of)
                .collect(Collectors.toList());
    }
}


