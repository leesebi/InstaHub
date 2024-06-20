package com.sparta.instahub.auth.dto;

import lombok.Getter;

@Getter
public class SignupRequestDto {
    String userId;
    String name;
    String email;
    String password;
    String introduction;
}
