package com.sparta.instahub.post.entity;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.comment.entity.Comment;
import com.sparta.instahub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
public class Post extends BaseEntity {

    // 기본 키
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자
    @ManyToOne
    @JoinColumn(name="userId",nullable = false)
    private User user;

    // 게시물 제목
    @Column(nullable = false)
    private String title;

    // 게시물 내용
    @Column(nullable = false)
    private String content;

    // 이미지 URl
    @Column
    private String imageUrl;

    //게시물에 달린 댓글 목록
    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 생성일시

    @Column(nullable = false)
    private LocalDateTime updatedAt; // 수정일시

    // 빌더 패턴을 사용한 생성자
    @Builder
    public Post(User user,String title, String content, String imageUrl) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdAt = LocalDateTime.now(); // 현재 시간을 생성일시로 설정
        this.updatedAt = LocalDateTime.now(); // 현재 시간을 수정일시로 설정
    }

    public Post() {

    }

    // 게시물 업데이트 메서드
    public void update(String title, String content, String imageUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.updatedAt = LocalDateTime.now(); // 현재 시간을 수정일시로 설정
    }
}
