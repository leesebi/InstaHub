package com.sparta.instahub.postLike.controller;

import com.sparta.instahub.postLike.dto.PostLikeResponseDto;
import com.sparta.instahub.postLike.service.PostLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostLikeController {
    private final PostLikeService service;

    @PostMapping("/post/{postId}/like")
    public ResponseEntity<PostLikeResponseDto> createLike(@PathVariable Long postId){
        return service.createLike(postId);
    }
}
