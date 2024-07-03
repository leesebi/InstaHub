package com.sparta.instahub.profile.entity;


import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.profile.dto.ProfileGetResponseDto;
import com.sparta.instahub.profile.dto.ProfileResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Profile {

    // 기본 키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 자기소개
    @Column
    private String introduction;

    // 이메일
    @Column
    private String email;

    // 주소
    @Column
    private String address;

    @Column
    private Integer commentLikeCount;

    @Column
    private Integer postLikeCount;

    // User와 1대1 관계
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void updateIntroduction(String introduction){
        this.introduction = introduction;
    }

    public void updateAddress(String address){
        this.address = address;
    }
    public void updateEmail(String email){
        this.email = email;
    }

    public void updateUser(User user) {
        this.user = user;
    }

    public User findUser() {
        return user;
    }
    @Builder
    public Profile(String email, String introduction, String address, User user) {
        this.email = email;
        this.introduction = introduction;
        this.address = address;
        this.user = user;
    }

    public void setCommentLikeCount() {
        this.commentLikeCount = user.getCommentLikes().size();
    }

    public void setPostLikeCount() {
        this.postLikeCount = user.getPostLikes().size();
    }



}
