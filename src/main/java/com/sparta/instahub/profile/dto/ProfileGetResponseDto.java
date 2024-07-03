package com.sparta.instahub.profile.dto;

import lombok.Builder;
import lombok.Getter;

@Getter

public class ProfileGetResponseDto {
    private String email;
    private String address;
    private String introduction;
    private Integer commentLikeCount;
    private Integer postLikeCount;


    @Builder
    public ProfileGetResponseDto(String email, String address, String introduction, Integer commentLikeCount, Integer postLikeCount) {
        this.email = email;
        this.address = address;
        this.introduction = introduction;
        this.commentLikeCount = commentLikeCount;
        this.postLikeCount = postLikeCount;
    }

}
