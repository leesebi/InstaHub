package com.sparta.instahub.commentLike.service;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.auth.repository.UserRepository;
import com.sparta.instahub.comment.Repository.CommentRepository;
import com.sparta.instahub.comment.entity.Comment;
import com.sparta.instahub.commentLike.dto.LikeResponseDto;
import com.sparta.instahub.commentLike.entity.CommentLike;
import com.sparta.instahub.commentLike.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    /***
     *
     * @param commentId : 좋아요 할 댓글의 아이디
     * @return 좋아요 확인
     */
    public ResponseEntity<LikeResponseDto> createLike(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("comment가 존재하지 않습니다.")
        );

        User user = findUser();

        checkCommentLike(user, commentId);

        comment.createLike(user);

        commentRepository.save(comment);

        LikeResponseDto responseDto = new LikeResponseDto("좋아요 성공");

        return ResponseEntity.ok(responseDto);
    }


    /***
     * @return : 현재 로그인 된 사용자
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
     * @param user 현재 로그인 된 사용자
     * @param commentId 현재 좋아요 할 댓글 Id
     */
    public void checkCommentLike(User user, Long commentId){
        List<Comment> userComments = user.getComments();
        List<CommentLike> userCommentLikes = user.getCommentLikes();
        int size = userCommentLikes.size();

        for (Comment loginUserComment : userComments) {
            if (Objects.equals(loginUserComment.getId(), commentId)) {
                throw new IllegalArgumentException("본인의 게시물에 좋아요를 할 수 없습니다.");
            }
        }

        for(int i=0; i<size; i++){
            CommentLike commentLike = userCommentLikes.get(i);
            if(Objects.equals(commentLike.getComment().getId(), commentId)){
                throw new IllegalArgumentException("이미 좋아요 한 게시글입니다.");
            }
        }

    }

}
