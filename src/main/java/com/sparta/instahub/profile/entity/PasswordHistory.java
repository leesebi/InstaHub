package com.sparta.instahub.profile.entity;

import com.sparta.instahub.auth.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class PasswordHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String password;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public PasswordHistory(String password, User user) {
        this.password = password;
        this.user = user;
    }
}
