package com.sparta.instahub.post.repository;

import com.sparta.instahub.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>{
}
