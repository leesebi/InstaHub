package com.sparta.instahub.post.controller;

import com.sparta.instahub.post.dto.PostRequestDto;
import com.sparta.instahub.post.dto.PostResponseDto;
import com.sparta.instahub.post.entity.Post;
import com.sparta.instahub.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

// 게시물 관련 요청을 처리하는 REST 컨트롤러
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;


    // 모든 게시물 조회 요청 처리
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<Post> posts = postService.getAllPosts(); // 모든 게시물 조회
        List<PostResponseDto> postResponseDtos = posts.stream()
                .map(post -> new PostResponseDto(post.getId(), post.getTitle(), post.getContent(), post.getUser().getUsername(), post.getImageUrl(), post.getCreatedAt(), post.getUpdatedAt()))
                .collect(Collectors.toList()); // 게시물 리스트를 DTO 리스트로 변환
        return new ResponseEntity<>(postResponseDtos, HttpStatus.OK);
    }

    // ID로 게시물 조회 요청 처리
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id); // ID로 게시물 조회
        PostResponseDto postResponseDto = new PostResponseDto(post.getId(), post.getTitle(), post.getContent(), post.getUser().getUsername(), post.getImageUrl(), post.getCreatedAt(), post.getUpdatedAt());
        return new ResponseEntity<>(postResponseDto, HttpStatus.OK);
    }

    // 새 게시물 생성 요청 처리
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto) {
        // 여기서는 임시로 작성자(author)를 설정없음
        Post post = postService.createPost(postRequestDto.getTitle(), postRequestDto.getContent(), postRequestDto.getImageUrl());
        PostResponseDto postResponseDto = new PostResponseDto(post.getId(), post.getTitle(), post.getContent(), post.getUser().getUsername(), post.getImageUrl(), post.getCreatedAt(), post.getUpdatedAt());
        return new ResponseEntity<>(postResponseDto, HttpStatus.CREATED);
    }

    // 게시물 수정 요청 처리
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto) {
        Post post = postService.updatePost(id, postRequestDto.getTitle(), postRequestDto.getContent(), postRequestDto.getImageUrl());
        PostResponseDto postResponseDto = new PostResponseDto(post.getId(), post.getTitle(), post.getContent(), post.getUser().getUsername(), post.getImageUrl(), post.getCreatedAt(), post.getUpdatedAt());
        return new ResponseEntity<>(postResponseDto, HttpStatus.OK);
    }

    // 게시물 삭제 요청 처리
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id); // 게시물 삭제
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
