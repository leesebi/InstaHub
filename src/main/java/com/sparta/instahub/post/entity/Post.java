package com.sparta.instahub.post.entity;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.comment.entity.Comment;
import com.sparta.instahub.common.entity.BaseEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Post extends BaseEntity {

    // 기본 키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자
    @ManyToOne
    @JoinColumn(name="userId",nullable = false)
    private User user;

    // 게시물 제목
    @Column(nullable = false)
    private String title;

    // 게시물 내용
    @Column(nullable = false)
    private String content;

    // 이미지 URl
    @Column
    private String imageUrl;

    //게시물에 달린 댓글 목록
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;
}
