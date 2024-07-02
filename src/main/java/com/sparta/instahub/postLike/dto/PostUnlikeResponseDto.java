package com.sparta.instahub.postLike.dto;

import lombok.Getter;

@Getter
public class PostUnlikeResponseDto {
    private String msg;

    public PostUnlikeResponseDto(String msg) {
        this.msg = msg;
    }
}
