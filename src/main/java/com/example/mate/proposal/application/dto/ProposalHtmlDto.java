package com.example.mate.proposal.application.dto;

import com.example.mate.proposal.domain.Proposal;

import java.util.List;
import java.util.stream.Collectors;


public record ProposalHtmlDto(
        Long id,
        String title,
        String description
) {
}


