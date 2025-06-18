package com.example.mate.proposal.presentation;

import com.example.mate.common.response.ApiResponse;
import com.example.mate.proposal.application.ProposalService;
import com.example.mate.proposal.application.dto.ProposalCreateUserDto;
import com.example.mate.proposal.application.dto.ProposalResponseDto;
import com.example.mate.proposal.application.dto.ProposalShareDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/v1/proposals/shared/")
@RequiredArgsConstructor
public class ProposalShareController {

    private final ProposalService proposalService;

    @PostMapping("{proposalId}")
    public String sharedProposal(
            Model model,
            @PathVariable Long proposalId,
            @RequestBody ProposalCreateUserDto request
    ) {
        ProposalResponseDto response = proposalService.getProposalByIdAndUserId(request.createuserid(), proposalId);

        model.addAttribute("title", response.title());
        model.addAttribute("htmlContent", response.description());

        return "proposal";
    }

    @GetMapping("{proposalId}")
    public String sharedProposals(
            Model model,
            @PathVariable Long proposalId
    ) {
        ProposalResponseDto response = proposalService.getProposalByIdAndUserId(1L, proposalId);

        model.addAttribute("title", response.title());
        model.addAttribute("htmlContent", response.description());

        return "proposal";
    }
}
