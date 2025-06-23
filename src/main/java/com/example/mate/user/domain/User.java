package com.example.mate.user.domain;

import com.example.mate.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kakao_id")
    private String kakaoId;

    private String nickname;

    @Column(name = "profile_url")
    private String profileUrl;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    private String reasons;

    @Column(columnDefinition = "TEXT", name = "reason_detail")
    private String reasonDetail;

    @Column(name = "job_type")
    private String jobType;

    @Column(name = "job_year")
    private Integer jobYear;

    @Column(columnDefinition = "TEXT", name = "intro")
    private String intro;

    @Column(name = "email")
    private String email;

    @Column(name = "contact")
    private String contact;

    @Column(name = "github_url")
    private String githubUrl;

    @Column(name = "blog_url")
    private String blogUrl;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserStack> userStacks = new ArrayList<>();

    @Column(name = "info_active", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean infoActive = false;

    @Builder
    public User(String kakaoId, String profileUrl) {
        this.kakaoId = kakaoId;
        this.profileUrl = profileUrl;
        this.nickname = DefaultNicknamePolicy.generatedRandomString();
        this.status = UserStatus.ACTIVE_FIRST_LOGIN;
        this.infoActive = false;
    }

    public User(Long id, String kakaoId, String nickname, String profileUrl, UserStatus status) {
        this.id = id;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.status = status;
    }

    public void updateUserInfo(String nickname, String profileUrl) {
        if (nickname != null) {
            this.nickname = nickname;
        }
        if (profileUrl != null) {
            this.profileUrl = profileUrl;
        }
        if (status.isFirstLogin()) {
            status = UserStatus.ACTIVE;
        }
    }

    public void updateUserInfoBasic(
            String nickname,
            String profileUrl
    ) {
        if (nickname != null) {
            this.nickname = nickname;
        }

        if (profileUrl != null) {
            this.profileUrl = profileUrl;
        }
    }

    public void updateUserInfoAll(
            String jobType,
            Integer jobYear,
            String intro,
            String email,
            String contact,
            String githubUrl,
            String blogUrl,
            Boolean infoActive
    ) {
        if (status.isFirstLogin()) {
            status = UserStatus.ACTIVE;
        }

        if (jobType != null) {
            this.jobType = jobType;
        }

        if (jobYear != null) {
            this.jobYear = jobYear;
        }

        if (intro != null) {
            this.intro = intro;
        }

        if (email != null) {
            this.email = email;
        }

        if (contact != null) {
            this.contact = contact;
        }

        if (githubUrl != null) {
            this.githubUrl = githubUrl;
        }

        if (blogUrl != null) {
            this.blogUrl = blogUrl;
        }

        if (infoActive != null) {
            this.infoActive = infoActive;
        }
    }


    public boolean isDeletedUser() {
        return status == UserStatus.DELETED;
    }

    public void updateActiveStatus() {
        this.status = UserStatus.ACTIVE;
    }

    public void withdrawWithAddReason(List<String> stringReasonTypeList, String detail) {
        this.reasons = stringReasonTypeList.stream()
                .map(ReasonType::getReasonTypeByString)
                .toList()
                .toString();
        this.reasonDetail = detail;
        this.status = UserStatus.DELETED;
    }

    public void addStack(Stack stack) {
        UserStack userStack = new UserStack(this, stack);
        userStacks.add(userStack);
    }

    public void removeStack(Stack stack) {
        userStacks.removeIf(userStack -> userStack.getStack().equals(stack));
    }

    public void syncStacks(List<Stack> newStacks) {
        List<Stack> currentStacks = userStacks.stream()
                .map(UserStack::getStack)
                .toList();

        List<Stack> toRemove = currentStacks.stream()
                .filter(existing -> !newStacks.contains(existing))
                .toList();

        List<Stack> toAdd = newStacks.stream()
                .filter(newStack -> !currentStacks.contains(newStack))
                .toList();

        toRemove.forEach(this::removeStack);
        toAdd.forEach(this::addStack);
    }
}
