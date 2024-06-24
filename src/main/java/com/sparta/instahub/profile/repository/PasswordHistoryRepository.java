package com.sparta.instahub.profile.repository;

import com.sparta.instahub.profile.entity.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {
    Long countBy();
}
