package com.example.mate.auth.application.dto;


import java.math.BigInteger;

public class SocialDto {
    private BigInteger socialId;
    private String nickname;
    private String image;

    public BigInteger getsocialId() {
        return socialId;
    }

    public void setsocialId(BigInteger socialId) {
        this.socialId = socialId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public SocialDto(BigInteger socialId, String nickname, String image) {
        this.socialId = socialId;
        this.nickname = nickname;
        this.image = image;
    }
}
