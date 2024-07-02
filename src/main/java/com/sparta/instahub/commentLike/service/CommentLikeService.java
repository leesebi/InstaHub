package com.sparta.instahub.commentLike.service;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.auth.repository.UserRepository;
import com.sparta.instahub.comment.Repository.CommentRepository;
import com.sparta.instahub.comment.entity.Comment;
import com.sparta.instahub.commentLike.dto.LikeResponseDto;
import com.sparta.instahub.commentLike.dto.UnLikeResponseDto;
import com.sparta.instahub.commentLike.entity.CommentLike;
import com.sparta.instahub.commentLike.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    /***
     * 좋아요 메소드
     * @param commentId
     * @return
     */
    @Transactional
    public LikeResponseDto createLike(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("comment가 존재하지 않습니다.")
        );

        CommentLike commentLike = new CommentLike();

        User user = findUser();

        checkCommentLike(user, commentId);

        commentLike.setComment(comment);
        likeRepository.save(commentLike);

        comment.increaseLike();

        return new LikeResponseDto("좋아요 성공");
    }

    /***
     * 좋아요 취소
     * @param likeId
     * @return
     */
    public UnLikeResponseDto deleteLike(Long likeId) {
        CommentLike commentLikes = likeRepository.findById(likeId).orElseThrow(
                () -> new IllegalArgumentException("like가 존재하지 않습니다.")
        );

        likeRepository.delete(commentLikes);

        return new UnLikeResponseDto("좋아요가 취소되었습니다.");
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


}
