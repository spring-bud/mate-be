package com.example.mate.auth.application.dto;


import java.math.BigInteger;

public class SocialDto {
    private BigInteger userId;
    private String name;
    private String nickname;
    private String image;
    private String email;

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SocialDto(BigInteger userId, String nickname, String image, String email) {
        this.userId = userId;
        this.nickname = nickname;
        this.image = image;
        this.email = email;
    }
}
