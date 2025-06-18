package com.example.mate.proposal.application.dto;


import com.example.mate.proposal.domain.Proposal;

public record ProposalShareDto(
        Long id,
        Long createUserId,
        String title,
        String description,
        String shareUrl
) {

    public static ProposalShareDto of(ProposalHtmlDto proposal, Long userId, String shareUrl) {
        return new ProposalShareDto(
                proposal.id(),
                userId,
                proposal.title(),
                proposal.description(),
                shareUrl
        );
    }
}


