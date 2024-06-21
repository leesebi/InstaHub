package com.sparta.instahub.auth.controller;

import com.sparta.instahub.auth.dto.RefreshTokenRequestDto;
import com.sparta.instahub.auth.dto.TokenResponseDto;
import com.sparta.instahub.auth.entity.LoginRequest;
import com.sparta.instahub.auth.entity.LoginResponse;
import com.sparta.instahub.auth.entity.SignupRequest;
import com.sparta.instahub.auth.jwt.JwtUtil;
import com.sparta.instahub.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입
     *
     * @param signupRequest
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest) {
        userService.signup(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
    }

    /**
     * 로그인
     *
     * @param loginRequest
     * @return 헤더에 반환
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse tokens = userService.login(loginRequest); // 로그인 시도 및 토큰 생성
        String accessToken = tokens.getAccessToken();
        String refreshToken = tokens.getRefreshToken();

        // 각 토큰을 별도의 헤더에 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Refresh-Token", refreshToken);

        return new ResponseEntity<>("로그인 성공", headers, HttpStatus.OK);
    }

    /**
     * 로그아웃
     *
     * @param userId
     * @param accessToken
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String userId, String accessToken) {
        userService.logout(userId, accessToken);
        return ResponseEntity.status(HttpStatus.OK).body("로그아웃 되었습니다");
    }

    /**
     * 회원탈퇴
     *
     * @param userId
     * @param accessToken
     * @param refreshToken
     * @return
     */
    @PatchMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam String userId, String password, String accessToken, String refreshToken) {
        userService.withdraw(userId, password, accessToken, refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body("회원탈퇴가 완료되었습니다.");
    }

    /**
     * 리프레시 토큰 재발급
     *
     * @param refreshTokenRequestDto
     * @return
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        TokenResponseDto tokenResponseDto = userService.refresh(refreshTokenRequestDto.getRefreshToken());
        return ResponseEntity.ok(tokenResponseDto);
    }
}
