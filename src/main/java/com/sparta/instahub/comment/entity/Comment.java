package com.sparta.instahub.comment.entity;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.comment.dto.CommentRequestDto;
import com.sparta.instahub.common.entity.BaseEntity;
import com.sparta.instahub.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name="comment")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//ddd
    //댓글이 달린 게시물
    @ManyToOne
    @JoinColumn(name="post_id",nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @Column(name = "contents", nullable = false)
    private String contents;

    public Comment(CommentRequestDto requestDto, Post post, User user) {
        this.contents=requestDto.getContents();
        this.post=post;
        this.user=user;
    }

    public void update(CommentRequestDto requestDto) {
        this.contents=requestDto.getContents();
    }
}
