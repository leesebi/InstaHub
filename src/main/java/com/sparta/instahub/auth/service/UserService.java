package com.sparta.instahub.auth.service;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User update(String userId, String newEmail, String newUserId) {
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("다시 확인해주세요")
        );

        user.updateUserId(newUserId);
        user.updateEmail(newEmail);

        return userRepository.save(user);
    }
}
