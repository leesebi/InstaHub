package com.sparta.instahub.auth.service;

import com.sparta.instahub.auth.dto.SignupRequestDto;
import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public ResponseEntity signup(SignupRequestDto requestDto) {

        // 회원 중복 확인
        Optional<User> checkUserId = userRepository.findByUserId(requestDto.getUserId());
        if (checkUserId.isPresent()) {
//            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
            return ResponseEntity.badRequest().body("중복된 사용자가 존재합니다.");
        }

        // email 중복 확인
        Optional<User> checkEmail = userRepository.findByEmail(requestDto.getEmail());
        if (checkEmail.isPresent()) {
            return ResponseEntity.badRequest().body("중복된 이메일이 존재합니다.");
        }

        // 사용자 등록
        User user = new User(requestDto.getUserId(), requestDto.getName(), requestDto.getEmail(), requestDto.getPassword(), requestDto.getIntroduction());
        userRepository.save(user);

        // 성공 메세지, 상태코드 반환
        return ResponseEntity.ok("회원가입에 성공하였습니다.");
    }
}
