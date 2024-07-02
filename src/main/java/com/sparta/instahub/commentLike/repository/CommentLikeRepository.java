package com.sparta.instahub.commentLike.repository;

import com.sparta.instahub.commentLike.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
}
