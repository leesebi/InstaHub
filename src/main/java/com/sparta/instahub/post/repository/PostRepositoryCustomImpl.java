package com.sparta.instahub.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.instahub.auth.entity.QUser;
import com.sparta.instahub.post.entity.Post;
import com.sparta.instahub.post.entity.QPost;
import com.sparta.instahub.postLike.entity.QPostLike;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    /***
     * 내가 좋아한 게시물 조회
     * @param userId
     * @param pageable
     * @return
     */
    @Override
    public List<Post> findWithLikeCountById(Long userId, Pageable pageable) {
        QPostLike postLike = QPostLike.postLike;
        QPost post = QPost.post;

        /***
         * select *
         * from post_like pl
         * join post p
         * on pl.post_id = p.id
         * where pl.user_id = 1
         */

        return queryFactory
                .select(post)
                .from(postLike)
                .join(postLike.post, post)
                .where(postLike.user.id.eq(userId))
                .orderBy(postLike.post.createdAt.asc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }
}
