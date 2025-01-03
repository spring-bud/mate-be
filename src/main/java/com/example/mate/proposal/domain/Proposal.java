package com.example.mate.proposal.domain;

import com.example.mate.common.domain.BaseTimeEntity;
import com.example.mate.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Proposal")
public class Proposal extends BaseTimeEntity {

    @Id
    @Column(name = "proposal_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "title")
    private String title;

    @Column(columnDefinition = "TEXT", name = "description")
    private String description;

    @Builder
    public Proposal(User user, String title, String description) {
        this.user = user;
        this.title = title;
        this.description = description;
    }

    public void updateProposalInfo(String title, String description) {
        if (title != null) {
            this.title = title;
        }
        if (description != null) {
            this.description = description;
        }
    }
}
