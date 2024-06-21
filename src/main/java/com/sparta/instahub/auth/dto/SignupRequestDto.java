package com.sparta.instahub.auth.dto;

import com.sparta.instahub.auth.entity.UserRole;
import com.sparta.instahub.auth.entity.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    private String userId;        // 사용자 ID
    private String email;         // 이메일
    private String password;      // 비밀번호
    private String username;      // 이름
    private UserRole userRole;    // 사용자 역할
    private UserStatus userStatus; // 사용자 상태
}

