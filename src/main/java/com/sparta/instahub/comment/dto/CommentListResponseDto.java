package com.sparta.instahub.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentListResponseDto {
    private Long id;
    private String contents;

    public CommentListResponseDto(Long id, String contents) {
        this.id = id;
        this.contents = contents;
    }
}
