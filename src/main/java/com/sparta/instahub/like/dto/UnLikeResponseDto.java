package com.sparta.instahub.like.dto;

import lombok.Getter;

@Getter
public class UnLikeResponseDto {
    private String msg;

    public UnLikeResponseDto(String msg){
        this.msg = msg;
    }
}
