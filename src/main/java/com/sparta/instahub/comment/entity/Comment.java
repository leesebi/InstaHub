package com.sparta.instahub.comment.entity;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.common.entity.BaseEntity;
import com.sparta.instahub.post.entity.Post;
import jakarta.persistence.*;

@Entity
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //댓글이 달린 게시물
    @ManyToOne
    @JoinColumn(name="post_id",nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @Column
    private String content;

}
