package com.sparta.instahub.auth.service;

import com.sparta.instahub.auth.dto.TokenResponseDto;
import com.sparta.instahub.auth.entity.LoginRequest;
import com.sparta.instahub.auth.entity.LoginResponse;
import com.sparta.instahub.auth.entity.SignupRequest;
import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.auth.entity.UserRole;
import com.sparta.instahub.auth.entity.UserStatus;

import java.util.List;

public interface UserService {
    // 사용자 정보 업데이트
    User update(String userId, String newEmail, String newUserId);

    // 회원가입
    void signup(SignupRequest signupRequest);

    // 로그인
    LoginResponse login(LoginRequest loginRequest);

    // 리프레시 토큰
    TokenResponseDto refresh(String refreshToken);

    // 로그아웃
    void logout(String userId, String accessToken);

    // 탈퇴
    void withdraw(String userId, String password, String accessToken, String refreshToken);

    User getCurrentAdmin();

    List<User> getAllUsers();

    User getUserById(Long id);

    User updateUser(Long id, String username, String email, UserRole userRole, UserStatus userStatus);

    void deleteUser(Long id);

    User promoteUserToAdmin(Long id);

    User blockUser(Long id);

    User unblockUser(Long id);


    User getUserByName(String username);

}
