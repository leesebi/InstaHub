package com.sparta.instahub.profile.entity;


import com.sparta.instahub.auth.entity.User;
import jakarta.persistence.*;

@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String introduction;

    @Column
    private String address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void updateIntroduction(String introduction){
        this.introduction = introduction;
    }

    public void updateAddress(String address){
        this.address = address;
    }
}
