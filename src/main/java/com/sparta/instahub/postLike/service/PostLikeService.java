package com.sparta.instahub.postLike.service;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.auth.repository.UserRepository;
import com.sparta.instahub.post.entity.Post;
import com.sparta.instahub.post.repository.PostRepository;
import com.sparta.instahub.postLike.dto.PostLikeResponseDto;
import com.sparta.instahub.postLike.entity.PostLike;
import com.sparta.instahub.postLike.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /***
     * 게시물 좋아요
     * @param postId
     * @return
     */
    public ResponseEntity<PostLikeResponseDto> createLike(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("postId를 다시 확인해주세요")
        );
        User user = findUser();

        checkPostLike(user, postId);

        post.createLike(user);
        postRepository.save(post);

        PostLikeResponseDto postLikeResponseDto = new PostLikeResponseDto("post 좋아요 성공");

        return ResponseEntity.ok(postLikeResponseDto);
    }

    /***
     * 현재 로그인된 사용자 찾기
     * @return
     */
    public User findUser(){
        Authentication loginUser = SecurityContextHolder.getContext().getAuthentication();
        String userName = loginUser.getName();

        User user = userRepository.findByUsername(userName).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 user입니다.")
        );

        return user;
    }

    /***
     * 좋아요가 가능한지 확인
     * @param user
     * @param postId
     */
    public void checkPostLike(User user, Long postId){
        // 본인의 게시물에 좋아요 X
        List<Post> postList = user.getPosts();
        for (Post post : postList) {
            if(Objects.equals(post.getId(), postId)){
                throw new IllegalArgumentException("본인 게시물에 좋아요 할 수 없습니다.");
            }
        }

        // 두번 좋아요 X
        List<PostLike> postLikes = user.getPostLikes();
        int size = postLikes.size();

        for (int i = 0; i < size; i++) {
            Long id = postLikes.get(i).getId();
            if(Objects.equals(postId, id)){
                throw new IllegalArgumentException("이미 좋아요 한 게시글입니다.");
            }
        }
    }
}
