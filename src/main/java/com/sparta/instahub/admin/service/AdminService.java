package com.sparta.instahub.admin.service;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.post.entity.Post;
import com.sparta.instahub.post.repository.PostRepository;
import com.sparta.instahub.auth.entity.entity.UserRole;
import com.sparta.instahub.auth.entity.entity.UserStatus;
import com.sparta.instahub.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 전체 회원 조회
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ID로 회원 조회
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
    }

    // 회원 정보 수정
    @Transactional
    public User updateUser(Long id, String username, String email, UserRole userRole, UserStatus userStatus) {
        User user = getUserById(id);
        User updatedUser = User.builder()
                .id(user.getId())
                .userId(user.getUserId())
                .email(email)
                .password(user.getPassword())
                .username(username)
                .introduction(user.getIntroduction())
                .userStatus(userStatus)
                .userRole(userRole)
                .refreshToken(user.getRefreshToken())
                .posts(user.getPosts())
                .comments(user.getComments())
                .build();
        return userRepository.save(updatedUser);
    }

    // 회원 삭제
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // 회원 운영진으로 승격
    @Transactional
    public User promoteUserToAdmin(Long id) {
        User user = getUserById(id);
        user.promoteToAdmin();
        return userRepository.save(user);
    }

    // 회원 차단
    @Transactional
    public User blockUser(Long id) {
        User user = getUserById(id);
        user.blockUser();
        return userRepository.save(user);
    }

    // 회원 차단 해제
    @Transactional
    public User unblockUser(Long id) {
        User user = getUserById(id);
        user.unblockUser();
        return userRepository.save(user);
    }

    // 공지글 등록
    @Transactional
    public Post createAnnouncement(String title, String content, String imageUrl) {
        User admin = getCurrentAdmin();
        Post post = Post.builder()
                .user(admin)
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .build();
        return postRepository.save(post);
    }

    // 모든 게시글 삭제
    @Transactional
    public void deleteAllPosts() {
        postRepository.deleteAll();
    }

    // 특정 게시글 삭제 (관리자)
    @Transactional
    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    // 현재 로그인된 관리자 가져오기
    private User getCurrentAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getUserRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("Current user is not an admin");
        }
        return user;
    }
}
