package com.sparta.instahub.like.dto;

import lombok.Getter;

@Getter
public class LikeResponseDto {
    private String msg;

    public LikeResponseDto(String msg) {
        this.msg = msg;
    }
}
