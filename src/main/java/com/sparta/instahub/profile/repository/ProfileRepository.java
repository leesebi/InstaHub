package com.sparta.instahub.profile.repository;

import com.sparta.instahub.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    // Long Id를 사용하여 User테이블에서 있는 profile 검색
    Optional<Profile> findByUser_Id(Long userId);
}
