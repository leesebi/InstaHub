package com.sparta.instahub.auth.service;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.auth.repository.UserRepository;
import com.sparta.instahub.profile.dto.PasswordRequestDto;
import com.sparta.instahub.profile.entity.PasswordHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public User update(Long id, String newEmail, String newUserId) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("다시 확인해주세요")
        );

        user.updateUserId(newUserId);
        user.updateEmail(newEmail);

        return userRepository.save(user);
    }

    // PasswordHistory에 비밀번호 저장
    public PasswordHistory savePasswordHistory() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication(); // 로그인 된 사용자

        return new PasswordHistory(user.getPassword(), user);
    }

    // 새로운 비밀번호 user에 저장
    public void updatePassword(PasswordRequestDto requestDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication();

        user.updatePassword(requestDto.getPassword());
        userRepository.save(user);
    }
}
