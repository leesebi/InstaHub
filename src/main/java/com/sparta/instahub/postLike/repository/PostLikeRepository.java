package com.sparta.instahub.postLike.repository;

import com.sparta.instahub.postLike.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
}
