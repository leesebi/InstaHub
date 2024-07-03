package com.sparta.instahub.post.repository;

import com.sparta.instahub.post.entity.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findWithLikeCountById(Long userId, Pageable pageable);
}
