package com.sparta.instahub.profile.dto;

import lombok.Getter;

@Getter
public class ProfileResponseDto {
    private String email;
    private String address;
    private String introduction;
    private String message;

    public ProfileResponseDto(String email, String address, String introduction, String message) {
        this.email = email;
        this.address = address;
        this.introduction = introduction;
        this.message = message;
    }
}
