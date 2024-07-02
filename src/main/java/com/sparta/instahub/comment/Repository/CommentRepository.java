package com.sparta.instahub.comment.Repository;

import com.sparta.instahub.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
