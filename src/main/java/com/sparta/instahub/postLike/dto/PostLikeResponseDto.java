package com.sparta.instahub.postLike.dto;

import lombok.Getter;

@Getter
public class PostLikeResponseDto {
    private String msg;

    public PostLikeResponseDto(String msg) {
        this.msg = msg;
    }
}
