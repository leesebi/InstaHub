package com.sparta.instahub.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    private String userId;        // 사용자 ID
    private String email;         // 이메일
    private String password;      // 비밀번호
    private String username;      // 이름
}
