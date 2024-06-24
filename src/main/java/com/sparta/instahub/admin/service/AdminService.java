package com.sparta.instahub.admin.service;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.auth.entity.UserRole;
import com.sparta.instahub.auth.entity.UserStatus;
import com.sparta.instahub.auth.service.UserServiceImpl;
import com.sparta.instahub.post.entity.Post;
import com.sparta.instahub.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface AdminService {

    void checkIfAdmin(User user);

    // 전체 회원 조회
    List<User> getAllUsers();

    // ID로 회원 조회
    @Transactional(readOnly = true)
    public User getUserById(Long id);

    // 회원 정보 수정
    User updateUser(Long id, String username, String email, UserRole userRole, UserStatus userStatus);

    // 회원 삭제
    void deleteUser(Long id);

    // 회원 운영진으로 승격
    @Transactional
    public User promoteUserToAdmin(Long id);

    // 회원 차단
    User blockUser(Long id);

    // 회원 차단 해제
    User unblockUser(Long id);

    // 공지글 등록
    Post createAnnouncement(String title, String content, MultipartFile imageUrl) throws IOException;

    // 모든 게시글 삭제
    void deleteAllPosts();

    // 특정 게시글 삭제 (관리자)
    void deletePost(Long postId);

    // 현재 로그인된 관리자 가져오기
    User getCurrentAdmin();
}
