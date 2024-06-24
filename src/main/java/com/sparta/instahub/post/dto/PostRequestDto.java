package com.sparta.instahub.post.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class PostRequestDto {
    private String title; // 게시물 제목
    private String content; // 게시물 내용
    private MultipartFile image;


    // 필드를 초기화
    public PostRequestDto(String title, String content, MultipartFile image) {
        this.title = title;
        this.content = content;
        this.image = image;
    }
}
