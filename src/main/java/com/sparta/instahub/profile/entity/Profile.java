package com.sparta.instahub.profile.entity;


import com.sparta.instahub.auth.entity.User;
import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Builder
public class Profile {

    // 기본 키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 자기소개
    @Column
    private String introduction;

    // 주소
    @Column
    private String address;

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

    public void updateUser(User user) {
        this.user = user;
    }
}
