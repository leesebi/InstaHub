package com.sparta.instahub.commentLike.controller;

import com.sparta.instahub.commentLike.dto.LikeResponseDto;
import com.sparta.instahub.commentLike.dto.UnLikeResponseDto;
import com.sparta.instahub.commentLike.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentLikeController {
    private final CommentLikeService service;
    @PostMapping("/comment/{commentId}/like")
    public ResponseEntity<LikeResponseDto> createCommentLike(@PathVariable Long commentId){
        LikeResponseDto responseDto = service.createLike(commentId);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/comment/delete/{likeId}")
    public ResponseEntity<UnLikeResponseDto> deleteCommentLike(@PathVariable Long likeId){
        UnLikeResponseDto responseDto = service.deleteLike(likeId);
        return ResponseEntity.ok(responseDto);
    }

}
