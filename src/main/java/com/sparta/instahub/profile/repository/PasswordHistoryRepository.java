package com.sparta.instahub.profile.repository;

import com.sparta.instahub.profile.entity.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
    // 최근 변경된 비밀번호 이력 상위 3개 조회
    List<PasswordHistory> findTop3ByOrderById();
}
