package com.sparta.instahub.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequestDto {
    @Size(min = 4, max = 10, message = "사용자 아이디는 최소 4자 이상, 10자 이하여야 합니다.")
    @Pattern(regexp = "^[a-z0-9]*$", message = "사용자 아이디는 알파벳 소문자(a~z), 숫자(0~9)로 구성되어야 합니다.")
    String userId;

    @Size(min=8, max=15, message = "비밀번호는 최소 8자 이상, 15자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_])\\S{10,}$", message = "비밀번호는 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자")
    String password;

    @NotBlank
    String name;

    @NotBlank
    String email;

    String introduction;
}
