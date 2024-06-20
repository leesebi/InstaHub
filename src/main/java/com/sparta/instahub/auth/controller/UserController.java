package com.sparta.instahub.auth.controller;

import com.sparta.instahub.auth.dto.SignupRequestDto;
import com.sparta.instahub.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /* 회원가입 */
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody SignupRequestDto requestDto) {
        return userService.signup(requestDto);
    }

}
