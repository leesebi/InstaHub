package com.sparta.instahub.profile.dto;

import lombok.Getter;

@Getter
public class PasswordResponseDto {
    String password;
    String msg;
    public PasswordResponseDto(PasswordRequestDto requestDto) {
        this.password = requestDto.getPassword();
        this.msg = "비밀번호 수정 완료되었습니다.";
    }
}
