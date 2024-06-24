package com.sparta.instahub.auth.service;

import com.sparta.instahub.auth.dto.TokenResponseDto;
import com.sparta.instahub.auth.entity.*;
import com.sparta.instahub.auth.jwt.JwtUtil;
import com.sparta.instahub.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public void signup(SignupRequest signupRequest) {
        String userId = signupRequest.getUserId();
        String password = signupRequest.getPassword();
        String email = signupRequest.getEmail();

        if (!userId.matches("^[a-zA-Z0-9]{10,20}$")) {
            throw new RuntimeException("아이디는 최소 10자 이상, 20자 이하이며 알파벳 대소문자와 숫자로 구성되어야 합니다.");
        }

        if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z\\d!@#$%^&*]{10,}$")) {
            throw new RuntimeException("비밀번호는 최소 10자 이상이어야 하며, 문자, 숫자, 특수문자를 포함해야 합니다.");
        }

        if (userRepository.findByUserId(userId).isPresent()) {
            throw new RuntimeException("중복된 사용자 ID가 존재합니다.");
        }

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("중복된 이메일이 존재합니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = User.builder()
                .userId(userId)
                .username(signupRequest.getUsername())
                .email(email)
                .password(encodedPassword)
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.USER)
                .build();
        userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("잘못된 비밀번호입니다.");
        }

        String accessToken = jwtUtil.createAccessToken(user.getUserId());
        String refreshToken = jwtUtil.createRefreshToken(user.getUserId());

        user.updateRefreshToken(refreshToken);
        user.login();
        userRepository.save(user);

        return new LoginResponse(accessToken, refreshToken);
    }

    /**
     * 로그아웃 메서드
     *
     * @param userId
     * @param accessToken
     */
    @Override
    public void logout(String userId, String accessToken) {
        // User 찾기
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );
        // logout
        user.logout();
        // user 정보 저장
        userRepository.save(user);
    }

    /**
     * 회원 탈퇴 (refreshToken 삭제)
     *
     * @param userId
     * @param accessToken
     * @param refreshToken
     */
    @Override
    public void withdraw(String userId, String password, String accessToken, String refreshToken) {
        // User 찾기
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );
        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        // 탈퇴
        user.withdraw();
        user.clearRefreshToken();
        // user 정보 저장
        userRepository.save(user);
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

    @Override
    public User getCurrentAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getUserRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("Current user is not an admin");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
    }

    @Override
    public User updateUser(Long id, String username, String email, UserRole userRole, UserStatus userStatus) {
        User user = getUserById(id);
        user.updateUserId(username);
        user.updateEmail(email);
        user.updateUserRole(userRole);
        user.updateUserStatus(userStatus);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User promoteUserToAdmin(Long id) {
        User user = getUserById(id);
        user.promoteToAdmin();
        return userRepository.save(user);
    }

    @Override
    public User blockUser(Long id) {
        User user = getUserById(id);
        user.blockUser();
        return userRepository.save(user);
    }

    @Override
    public User unblockUser(Long id) {
        User user = getUserById(id);
        user.unblockUser();
        return userRepository.save(user);
    }

    public User getUserByName(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
