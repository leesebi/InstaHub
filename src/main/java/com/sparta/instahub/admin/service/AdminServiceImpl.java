package com.sparta.instahub.admin.service;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.auth.entity.UserRole;
import com.sparta.instahub.auth.entity.UserStatus;
import com.sparta.instahub.auth.service.UserServiceImpl;
import com.sparta.instahub.exception.UnauthorizedException;
import com.sparta.instahub.post.entity.Post;
import com.sparta.instahub.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final PostService postService;
    private final UserServiceImpl userService;

    public void checkIfAdmin(User user) {
        if (user.getUserRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("Access Denied: admin만 접근할수 있습니다.");
        }
    }

    // 전체 회원 조회
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        User currentAdmin = getCurrentAdmin();
        checkIfAdmin(currentAdmin);
        return userService.getAllUsers();
    }

    // ID로 회원 조회
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        User currentAdmin = getCurrentAdmin();
        checkIfAdmin(currentAdmin);
        return userService.getUserById(id);
    }

    // 회원 정보 수정
    @Transactional
    public User updateUser(Long id, String username, String email, UserRole userRole, UserStatus userStatus) {
        User currentAdmin = getCurrentAdmin();
        checkIfAdmin(currentAdmin);
        return userService.updateUser(id, username, email, userRole, userStatus);
    }

    // 회원 삭제
    @Transactional
    public void deleteUser(Long id) {
        User currentAdmin = getCurrentAdmin();
        checkIfAdmin(currentAdmin);
        userService.deleteUser(id);
    }

    // 회원 운영진으로 승격
    @Transactional
    public User promoteUserToAdmin(Long id) {
        User currentAdmin = getCurrentAdmin();
        checkIfAdmin(currentAdmin);
        return userService.promoteUserToAdmin(id);
    }

    // 회원 차단
    @Transactional
    public User blockUser(Long id) {
        User currentAdmin = getCurrentAdmin();
        checkIfAdmin(currentAdmin);
        return userService.blockUser(id);
    }

    // 회원 차단 해제
    @Transactional
    public User unblockUser(Long id) {
        User currentAdmin = getCurrentAdmin();
        checkIfAdmin(currentAdmin);
        return userService.unblockUser(id);
    }

    // 공지글 등록
    @Transactional
    public Post createAnnouncement(String title, String content, MultipartFile imageUrl) {
        User currentAdmin = getCurrentAdmin();
        checkIfAdmin(currentAdmin);
        return postService.createPost(title, content, imageUrl, currentAdmin.getUsername());
    }


    // 모든 게시글 삭제
    @Transactional
    public void deleteAllPosts() {
        User currentAdmin = getCurrentAdmin();
        checkIfAdmin(currentAdmin);
        postService.deleteAllPosts();
    }

    // 특정 게시글 삭제 (관리자)
    @Transactional
    public void deletePost(Long postId) {
        User currentAdmin = getCurrentAdmin();
        checkIfAdmin(currentAdmin);
        postService.deletePost(postId, currentAdmin.getUsername());
    }

    // 현재 로그인된 관리자 가져오기
    public User getCurrentAdmin() {
        return userService.getCurrentAdmin();
    }
}

