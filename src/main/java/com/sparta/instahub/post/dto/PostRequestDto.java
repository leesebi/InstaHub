package com.sparta.instahub.post.dto;

import lombok.Getter;

@Getter
public class PostRequestDto {
    private String title; // 게시물 제목
    private String content; // 게시물 내용
    private String imageUrl;


    // 필드를 초기화
    public PostRequestDto(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}
