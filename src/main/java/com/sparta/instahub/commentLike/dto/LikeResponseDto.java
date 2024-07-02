package com.sparta.instahub.commentLike.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class LikeResponseDto {
    private String msg;

    public LikeResponseDto(String msg) {
        this.msg = msg;
    }
}
