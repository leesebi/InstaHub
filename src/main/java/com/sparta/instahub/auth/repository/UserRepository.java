package com.sparta.instahub.auth.repository;

import com.sparta.instahub.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 사용자명을 통해 사용자 정보를 가져오는 메서드
    Optional<User> findByUsername(String name);

    // 사용자 아이디를 통해 사용자 정보를 가져오는 메서드
    Optional<User> findByUserId(String userId);

    // 사용자 이메일을 통해 사용자 정보를 가져오는 메서드
    Optional<User> findByEmail(String email);
}
