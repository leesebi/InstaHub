package com.sparta.instahub.auth.service;

import com.sparta.instahub.auth.dto.TokenResponseDto;
import com.sparta.instahub.auth.entity.*;
import com.sparta.instahub.auth.jwt.JwtUtil;
import com.sparta.instahub.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public User update(String userId, String newEmail, String newUserId) {

        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("다시 확인해주세요")
        );

        user.updateUserId(newUserId);
        user.updateEmail(newEmail);

        return userRepository.save(user);
    }

    /**
     * 회원가입 메서드
     *
     * @param signupRequest
     */
    @Override
    public void signup(SignupRequest signupRequest) {
        String userId = signupRequest.getUserId();
        String password = signupRequest.getPassword();
        String email = signupRequest.getEmail();

        // ID 패턴 체크
        if (!userId.matches("^[a-zA-Z0-9]{10,20}$")) {
            throw new RuntimeException("아이디는 최소 10자 이상, 20자 이하이며 알파벳 대소문자와 숫자로 구성되어야 합니다.");
        }

        // 비밀번호 패턴 체크
        if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z\\d!@#$%^&*]{10,}$")) {
            throw new RuntimeException("비밀번호는 최소 10자 이상이어야 하며, 문자, 숫자, 특수문자를 포함해야 합니다.");
        }

        // 사용자 ID 중복 체크
        if (userRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("중복된 사용자 ID가 존재합니다.");
        }

        // 이메일 중복 체크
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("중복된 이메일이 존재합니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);
        User user = User.builder()
                .userId(userId)
                .username(signupRequest.getUsername())
                .email(email)
                .password(encodedPassword)
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.USER) // 기본 사용자 역할
                .build();
        userRepository.save(user);
    }

    /**
     * 로그인 메서드
     *
     * @param loginRequest
     * @return LoginResponse
     */
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUserId(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("잘못된 비밀번호입니다.");
        }

        String accessToken = jwtUtil.createAccessToken(user.getUserId());
        String refreshToken = jwtUtil.createRefreshToken(user.getUserId());

        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        return new LoginResponse(accessToken, refreshToken);
    }

    // logout method
    @Override
    public void logout(String userId, String accessToken) {
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );
        user.logout();
    }

    /**
     * 리프레시 토큰으로 토큰 재발급
     *
     * @param refreshToken
     * @return TokenResponseDto
     */
    @Override
    public TokenResponseDto refresh(String refreshToken) {
        String username = jwtUtil.getUsernameFromToken(refreshToken);
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!refreshToken.equals(user.getRefreshToken())) {
            throw new RuntimeException("잘못된 리프레시 토큰입니다.");
        }

        String newAccessToken = jwtUtil.createAccessToken(username);
        String newRefreshToken = jwtUtil.createRefreshToken(username);

        user.updateRefreshToken(newRefreshToken);
        userRepository.save(user);

        return new TokenResponseDto(newAccessToken, newRefreshToken);
    }
}
