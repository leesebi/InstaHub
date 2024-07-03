package com.sparta.instahub.comment.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.instahub.comment.entity.Comment;
import com.sparta.instahub.like.entity.QCommentLike;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sparta.instahub.comment.entity.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Comment> findCommentWithLikeById(Long userId, Pageable pageable) {
        QCommentLike commentLike = QCommentLike.commentLike;

        return jpaQueryFactory
                .select(comment)
                .from(commentLike)
                .join(commentLike.comment, comment)
                .where(commentLike.user.id.eq(userId))
                .orderBy(comment.createAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
