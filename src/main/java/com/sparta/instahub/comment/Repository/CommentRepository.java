package com.sparta.instahub.comment.Repository;

import com.sparta.instahub.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
