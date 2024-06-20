package com.sparta.instahub.admin.controller;

import com.sparta.instahub.admin.service.AdminService;
import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.post.dto.PostRequestDto;
import com.sparta.instahub.post.dto.PostResponseDto;
import com.sparta.instahub.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    // 전체 회원 조회
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // ID로 회원 조회
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = adminService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // 회원 정보 수정
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userRequest) {
        User updatedUser = adminService.updateUser(id, userRequest.getUsername(), userRequest.getEmail(), userRequest.getUserRole(), userRequest.getUserStatus());
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    // 회원 삭제
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 회원 운영진으로 변경
    @PutMapping("/users/{id}/promote")
    public ResponseEntity<User> promoteUserToAdmin(@PathVariable Long id) {
        User promotedUser = adminService.promoteUserToAdmin(id);
        return new ResponseEntity<>(promotedUser, HttpStatus.OK);
    }

    // 회원 차단
    @PutMapping("/users/{id}/block")
    public ResponseEntity<User> blockUser(@PathVariable Long id) {
        User blockedUser = adminService.blockUser(id);
        return new ResponseEntity<>(blockedUser, HttpStatus.OK);
    }

    // 회원 차단 해제
    @PutMapping("/users/{id}/unblock")
    public ResponseEntity<User> unblockUser(@PathVariable Long id) {
        User unblockedUser = adminService.unblockUser(id);
        return new ResponseEntity<>(unblockedUser, HttpStatus.OK);
    }

    // 공지글 등록
    @PostMapping("/announcement")
    public ResponseEntity<PostResponseDto> createAnnouncement(@RequestBody PostRequestDto postRequestDto) {
        Post post = adminService.createAnnouncement(postRequestDto.getTitle(), postRequestDto.getContent(), postRequestDto.getImageUrl());
        PostResponseDto postResponseDto = PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getUser().getUsername())
                .imageUrl(post.getImageUrl())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
        return new ResponseEntity<>(postResponseDto, HttpStatus.CREATED);
    }
}

