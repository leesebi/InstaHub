package com.sparta.instahub.auth.entity;

import com.sparta.instahub.comment.entity.Comment;
import com.sparta.instahub.common.entity.BaseEntity;
import com.sparta.instahub.post.entity.Post;
import com.sparta.instahub.user.entity.UserRole;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class User extends BaseEntity {

    // 기본키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자 ID
    @Column(nullable = false, unique = true)
    private String userId;

    // 이메일
    @Column(nullable = false, unique = true)
    private String email;

    // 비밀번호
    @Column(nullable = false)
    private String password;

    // 이름
    @Column(nullable = false)
    private String username;

    // 자기소개 - null 허용
    @Column
    private String introduction;

    // 사용자 상태
    @Column(nullable = false)
    private String userStatus;

    // 사용자 역할
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    // refreshToken
    @Column
    private String refreshToken;

    // User와 Post는 일대다 관계
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts; // 사용자가 작성한 게시물 목록

    // User와 Comment는 일대다 관계
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments; // 사용자가 작성한 댓글 목록
}