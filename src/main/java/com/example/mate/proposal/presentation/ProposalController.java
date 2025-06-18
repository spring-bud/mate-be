package com.example.mate.proposal.presentation;

import com.example.mate.common.response.ApiResponse;
import com.example.mate.proposal.application.ProposalService;
import com.example.mate.proposal.application.dto.ProposalCreateUserDto;
import com.example.mate.proposal.application.dto.ProposalHtmlDto;
import com.example.mate.proposal.application.dto.ProposalResponseDto;
import com.example.mate.proposal.application.dto.ProposalShareDto;
import com.example.mate.proposal.domain.Proposal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/proposals")
@RequiredArgsConstructor
public class ProposalController {

    private final ProposalService proposalService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProposalResponseDto>> createProposal(
            @AuthenticationPrincipal Long userId,
            @RequestBody ProposalResponseDto request
    ) {
        ProposalResponseDto proposalResponseDto = proposalService.createProposal(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(proposalResponseDto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProposalResponseDto>>> getProposal(
            @AuthenticationPrincipal Long userId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                proposalService.getProposalByUserId(userId))
        );
    }

    @GetMapping("/{proposalId}")
    public ResponseEntity<ApiResponse<ProposalResponseDto>> getProposalById(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long proposalId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                proposalService.getProposalByIdAndUserId(userId, proposalId))
        );
    }

    @PatchMapping("/{proposalId}")
    public ResponseEntity<ApiResponse<ProposalResponseDto>> updateProposalById(
            @AuthenticationPrincipal Long userId,
            @RequestBody ProposalResponseDto request,
            @PathVariable Long proposalId
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                proposalService.updateProposalByIdAndUserId(userId, proposalId, request))
        );
    }

    @DeleteMapping("/{proposalId}")
    public ResponseEntity<Void> deleteProposalById(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long proposalId
    ) {
        proposalService.deleteProposalByIdAndUserId(userId, proposalId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("share/{proposalId}")
    public ResponseEntity<ProposalShareDto> shareProposal(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long proposalId
    ) {
        ProposalShareDto response = proposalService.getProposalShareContent(proposalId, userId);

        return ResponseEntity.ok(response);
    }
}
