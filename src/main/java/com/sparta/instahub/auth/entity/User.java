package com.sparta.instahub.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sparta.instahub.comment.entity.Comment;
import com.sparta.instahub.common.entity.BaseEntity;
import com.sparta.instahub.like.entity.CommentLike;
import com.sparta.instahub.like.entity.PostLike;
import com.sparta.instahub.post.entity.Post;
import com.sparta.instahub.profile.entity.PasswordHistory;
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
@JsonIgnoreProperties({"posts", "comments", "profile"})
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
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Profile profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentLike> commentLikes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> postLikes;

    public User(String userId, String name, String email, String password) {
        this.userId = userId;
        this.username = name;
        this.email = email;
        this.password = password;
    }
    // User와 PasswordHistroy는 1대다 관계
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PasswordHistory> passwordHistories; // 사용자가 작성한 비밀번호 목록

    public void updateProfile(Profile profile) {
        this.profile = profile;
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

    // 비밀번호 업데이트
    public void updatePassword(String password){
        this.password = password;
    }

    // 로그인 상태 변경
    public void login() {
        this.userStatus = UserStatus.ACTIVE;
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

    public void updateUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void updateUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    // 토큰 삭제
    public void clearRefreshToken() {
        this.refreshToken = null;
    }

    public void updateUserInfo(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.userRole = user.getUserRole();
        this.profile = user.getProfile();
        this.userStatus = user.getUserStatus();
    }

}