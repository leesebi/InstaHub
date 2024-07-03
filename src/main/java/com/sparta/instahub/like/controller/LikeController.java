package com.sparta.instahub.like.controller;

import com.sparta.instahub.like.dto.LikeResponseDto;
import com.sparta.instahub.like.dto.UnLikeResponseDto;
import com.sparta.instahub.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService service;
    @PostMapping("/comment/{commentId}/like")
    public ResponseEntity<LikeResponseDto> createCommentLike(@PathVariable Long commentId){
        LikeResponseDto responseDto = service.createLikeComment(commentId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/comment/delete/{likeId}")
    public ResponseEntity<UnLikeResponseDto> deleteCommentLike(@PathVariable Long likeId){
        UnLikeResponseDto responseDto = service.deleteLikeComment(likeId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/post/{postId}/like")
    public ResponseEntity<LikeResponseDto> createLike(@PathVariable Long postId){
        LikeResponseDto responseDto = service.createLikePost(postId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/post/unlike/{likeId}")
    public ResponseEntity<UnLikeResponseDto> deleteLike(@PathVariable Long likeId){
        UnLikeResponseDto responseDto = service.deleteLikePost(likeId);
        return ResponseEntity.ok(responseDto);
    }

}
