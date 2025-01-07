package com.example.mate.proposal.application;

import com.example.mate.proposal.application.dto.ProposalResponseDto;
import com.example.mate.proposal.domain.Proposal;
import com.example.mate.proposal.domain.repository.ProposalRepository;
import com.example.mate.proposal.exception.ProposalException;
import com.example.mate.user.application.UserService;
import com.example.mate.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.mate.proposal.exception.ProposalExceptionType.NOT_EXIST_Proposal;


@Service
@RequiredArgsConstructor
public class ProposalService {

    private final ProposalRepository proposalRepository;
    private final UserService userService;

    @Transactional
    public ProposalResponseDto createProposal(Long userId, ProposalResponseDto request) {
        User findUser = userService.getUserById(userId);

        Proposal newProposal = Proposal.builder()
                .user(findUser)
                .title(request.title())
                .description(request.description())
                .build();

        Proposal saveProposal = proposalRepository.save(newProposal);

        return ProposalResponseDto.of(saveProposal);
    }

    public List<ProposalResponseDto> getProposalByUserId(Long userId) {
        List<Proposal> findProposals = proposalRepository.findByUserId(userId);

        return ProposalResponseDto.of(findProposals);
    }

    public ProposalResponseDto getProposalByIdAndUserId(Long userId, Long ProposalId) {
        Proposal findProposal = proposalRepository.findByIdAndUserId(ProposalId, userId)
                .orElseThrow(() -> new ProposalException(NOT_EXIST_Proposal));

        return ProposalResponseDto.of(findProposal);
    }

    @Transactional
    public ProposalResponseDto updateProposalByIdAndUserId(Long userId, Long ProposalId, ProposalResponseDto request) {
        Proposal findProposal = proposalRepository.findByIdAndUserId(ProposalId, userId)
                .orElseThrow(() -> new ProposalException(NOT_EXIST_Proposal));

        findProposal.updateProposalInfo(
                request.title(),
                request.description()
        );

        Proposal updateProposal = proposalRepository.save(findProposal);

        return ProposalResponseDto.of(updateProposal);
    }

    @Transactional
    public void deleteProposalByIdAndUserId(Long userId, Long ProposalId) {
        Proposal findProposal = proposalRepository.findByIdAndUserId(ProposalId, userId)
                .orElseThrow(() -> new ProposalException(NOT_EXIST_Proposal));

        proposalRepository.delete(findProposal);
    }
}
