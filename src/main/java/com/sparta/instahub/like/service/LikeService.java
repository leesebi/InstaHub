package com.sparta.instahub.like.service;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.auth.repository.UserRepository;
import com.sparta.instahub.comment.Repository.CommentRepository;
import com.sparta.instahub.comment.entity.Comment;
import com.sparta.instahub.like.dto.LikeResponseDto;
import com.sparta.instahub.like.dto.UnLikeResponseDto;
import com.sparta.instahub.like.entity.CommentLike;
import com.sparta.instahub.like.entity.PostLike;
import com.sparta.instahub.like.repository.CommentLikeRepository;
import com.sparta.instahub.like.repository.PostLikeRepository;
import com.sparta.instahub.post.entity.Post;
import com.sparta.instahub.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final CommentLikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    /***
     * 좋아요 메소드
     * @param commentId
     * @return
     */
    @Transactional
    public LikeResponseDto createLikeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("comment가 존재하지 않습니다.")
        );

        User user = findUser();

        checkCommentLike(user, commentId);

        CommentLike commentLike = new CommentLike(comment, user);
        likeRepository.save(commentLike);

        int count = comment.getLikes().size();
        comment.setLikeCount(count);

        return new LikeResponseDto("좋아요 성공");
    }

    /***
     * 좋아요 취소
     * @param likeId
     * @return
     */
    public UnLikeResponseDto deleteLikeComment(Long likeId) {
        CommentLike commentLikes = likeRepository.findById(likeId).orElseThrow(
                () -> new IllegalArgumentException("like가 존재하지 않습니다.")
        );

        Comment comment = commentLikes.getComment();
        int likeCount = comment.getLikeCount() - 1;
        comment.setLikeCount(likeCount);

        likeRepository.delete(commentLikes);


        return new UnLikeResponseDto("좋아요가 취소되었습니다.");
    }

    /***
     * 좋아요 가능한 지 확인
     * @param user
     * @param commentId
     */
    public void checkCommentLike(User user, Long commentId){
        // 본인의 게시물에 좋아요 X
        List<Comment> userComments = user.getComments();

        for (Comment loginUserComment : userComments) {
            if (Objects.equals(loginUserComment.getId(), commentId)) {
                throw new IllegalArgumentException("본인의 게시물에 좋아요를 할 수 없습니다.");
            }
        }

        // 두번 좋아요 X
        List<CommentLike> userCommentLikes = user.getCommentLikes();
        int size = userCommentLikes.size();
        for(int i=0; i<size; i++){
            CommentLike commentLike = userCommentLikes.get(i);
            if(Objects.equals(commentLike.getComment().getId(), commentId)){
                throw new IllegalArgumentException("이미 좋아요 한 게시글입니다.");
            }
        }

    }

    /***
     * 게시물 좋아요
     * @param postId
     * @return
     */
    @Transactional
    public LikeResponseDto createLikePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("postId를 다시 확인해주세요")
        );
        User user = findUser();

        checkPostLike(user, postId);

        post.createLike(user);
        postRepository.save(post);

        int likeCount = post.postLikes.size();
        post.setLikeCount(likeCount);

        return new LikeResponseDto("post 좋아요 성공");
    }

    /***
     * 좋아요 취소 메소드
     * @param likeId
     * @return
     */
    @Transactional
    public UnLikeResponseDto deleteLikePost(Long likeId) {
        PostLike postLike = postLikeRepository.findById(likeId).orElseThrow(
                () -> new IllegalArgumentException("likeId를 다시 확인해주세요")
        );

        int likeCount = postLike.getPost().postLikes.size()-1;
        postLike.getPost().setLikeCount(likeCount);

        postLikeRepository.delete(postLike);

        return new UnLikeResponseDto("post 좋아요 취소 성공");
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
            Long id = postLikes.get(i).getPost().getId();
            if(Objects.equals(postId, id)){
                throw new IllegalArgumentException("이미 좋아요 한 게시글입니다.");
            }
        }
    }

    /***
     * 현재 로그인중인 사용자 찾기
     * @return
     */
    public User findUser(){
        Authentication loginUser = SecurityContextHolder.getContext().getAuthentication();
        String userName = loginUser.getName();

        User user = userRepository.findByUsername(userName).orElseThrow(
                () -> new IllegalArgumentException("사용할수 없는 사용자입니다.")
        );

        return user;
    }

}
