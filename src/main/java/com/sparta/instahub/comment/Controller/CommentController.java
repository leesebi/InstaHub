package com.sparta.instahub.comment.Controller;

import com.sparta.instahub.comment.dto.CommentRequestDto;
import com.sparta.instahub.comment.dto.CommentResponseDto;
import com.sparta.instahub.comment.entity.Comment;
import com.sparta.instahub.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class CommentController {

    private final CommentService commentService;

    //댓글 작성
    @PostMapping("/{postId}/comments")
    public CommentResponseDto createComment(@PathVariable Long postId,
                                            @RequestBody CommentRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetails userDetails){
        return commentService.createComment(postId, requestDto,userDetails.getUsername());
    }

    //댓글 조회
    @GetMapping("/{postId}/comments")
    public List<CommentResponseDto> getAllComment(@PathVariable Long postId) {
        return commentService.getAllComment(postId);
    }

    //댓글 수정
    @PutMapping("/{postId}/comments/{commentId}")
    public CommentResponseDto updateComment(@PathVariable Long postId,
                                            @PathVariable Long commentId,
                                            @RequestBody CommentRequestDto requestDto,
                                            @AuthenticationPrincipal UserDetails userDetails){
        return commentService.updateComment(postId, requestDto,userDetails.getUsername());

    }

    //댓글 삭제
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId,
                                                @AuthenticationPrincipal UserDetails userDetails){
        return commentService.deleteComment(commentId, userDetails.getUsername());
    }


}
