package com.sparta.instahub.post.service;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.auth.entity.UserRole;
import com.sparta.instahub.auth.service.UserServiceImpl;
import com.sparta.instahub.post.entity.Post;
import com.sparta.instahub.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Post 엔티티에 대한 비즈니스 로직을 처리하는 서비스 클래스
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserServiceImpl userService;

    // 모든 게시물 조회
    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    // ID로 게시물 조회
    @Override
    @Transactional(readOnly = true)
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
    }

    // 새 게시물 생성
    @Override
    @Transactional
    public Post createPost(String title, String content, String imageUrl, String username) {
        // 현재 로그인된 사용자 가져오기
        User user = userService.getUserByName(username);

        Post post = Post.builder()
                .user(user)
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .build();
        return postRepository.save(post); // Post 객체 저장
    }

    // 게시물 수정
    @Override
    @Transactional
    public Post updatePost(Long id, String title, String content, String imageUrl, String username) {
        // 현재 로그인된 사용자 가져오기
        User currentUser = userService.getUserByName(username);
        Post post = getPostById(id); // ID로 게시물 조회

        if (!post.getUser().equals(currentUser)) {
            throw new IllegalArgumentException("You are not authorized to update this post");
        }
        post.update(title, content, imageUrl); // 게시물 업데이트
        return postRepository.save(post); // 업데이트된 게시물 저장
    }

    // 게시물 삭제
    @Override
    @Transactional
    public void deletePost(Long id, String username) {
        User currentUser = userService.getUserByName(username);
        Post post = getPostById(id); // ID로 게시물 조회

        // 현재 로그인된 사용자가 게시글 작성자이거나 관리자인지 확인
        if (!post.getUser().equals(currentUser) && currentUser.getUserRole() != UserRole.ADMIN) {
            throw new IllegalArgumentException("You are not authorized to delete this post");
        }

        postRepository.deleteById(id); // ID로 게시물 삭제
    }

    // 모든 게시물 삭제
    @Override
    @Transactional
    public void deleteAllPosts() {
        postRepository.deleteAll();
    }
}
