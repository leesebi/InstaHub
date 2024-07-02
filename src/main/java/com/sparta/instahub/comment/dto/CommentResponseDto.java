package com.sparta.instahub.comment.dto;

import com.sparta.instahub.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long id;
    private String contents;
    private Integer likeCount;

    public CommentResponseDto(Comment comment) {
        this.id=comment.getId();
        this.contents=comment.getContents();
        this.likeCount = comment.getLikeCount();
    }
}
