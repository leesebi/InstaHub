package com.sparta.instahub.auth.entity;

import com.sparta.instahub.comment.entity.Comment;
import com.sparta.instahub.common.entity.BaseEntity;
import com.sparta.instahub.post.entity.Post;
import com.sparta.instahub.profile.entity.Profile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    // 사용자 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus userStatus;

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

    // User와 Profile 1대1 관계
    @OneToOne(mappedBy = "user")
    private Profile profile;

    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.username = name;
        this.email = email;
        this.password = password;
    }

    // 사용자 역할 및 상태를 업데이트
    public void promoteToAdmin() {
        this.userRole = UserRole.ADMIN;
    }

    // 차단하기
    public void blockUser() {
        this.userStatus = UserStatus.BLOCKED;
    }

    // 차단 풀기
    public void unblockUser() {
        this.userStatus = UserStatus.ACTIVE;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateUserId(String userId) {
        this.userId = userId;
    }

    // 로그아웃 (UserStatus 변경)
    public void logout() {
        this.userStatus = UserStatus.LOGOUT; // userStatus를 LOGOUT으로 변경
    }

    // 탈퇴 (UserStatus 변경)
    public void withdraw() {
        this.userStatus = UserStatus.WITHDRAWN;
    }

    // 리프레시 토큰 업데이트
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // 토큰 삭제
    public void clearToken(String refreshToken) {
        this.refreshToken = null;
    }
}