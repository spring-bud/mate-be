package com.example.mate.review.domain;

public enum ReviewStatus {
    ACTIVE("활성화된 계정"),
    DELETED("삭제된 계정"),
    ;

    private final String description;

    public String description() {
        return description;
    }

    ReviewStatus(String description) {
        this.description = description;
    }

}
