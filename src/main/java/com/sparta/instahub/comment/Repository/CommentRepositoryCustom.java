package com.sparta.instahub.comment.Repository;

import com.sparta.instahub.comment.entity.Comment;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentRepositoryCustom{
    List<Comment> findCommentWithLikeById(Long userId, Pageable pageable);
}
