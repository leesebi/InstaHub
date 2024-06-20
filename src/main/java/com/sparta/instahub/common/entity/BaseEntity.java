package com.sparta.instahub.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {

    // 엔티티 생성 시점 저장
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    // 엔티티 마지막 수정 시점 저장
    private LocalDateTime updateAt;

    // 엔티티가 저장되기전
    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }
}
