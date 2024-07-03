package com.sparta.instahub.profile.service;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.auth.repository.UserRepository;
import com.sparta.instahub.auth.service.UserService;
import com.sparta.instahub.profile.dto.PasswordRequestDto;
import com.sparta.instahub.profile.dto.PasswordResponseDto;
import com.sparta.instahub.profile.dto.ProfileGetResponseDto;
import com.sparta.instahub.profile.dto.ProfileRequestDto;
import com.sparta.instahub.profile.entity.PasswordHistory;
import com.sparta.instahub.profile.entity.Profile;
import com.sparta.instahub.profile.repository.PasswordHistoryRepository;
import com.sparta.instahub.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    // 프로필 수정
    @Transactional
    public Profile updateProfile(Long id, ProfileRequestDto requestDto) {
        log.info("update profile" + id);

        Profile profile = profileRepository.findByUser_Id(id).orElseThrow(
                () -> new IllegalArgumentException("다시 확인해 주세요")
        );
        profile.updateEmail(requestDto.getEmail());
        profile.updateAddress(requestDto.getAddress());
        profile.updateIntroduction(requestDto.getIntroduction());

        User user = profile.findUser();
        user.updateProfile(profile);
        user.updateEmail(requestDto.getEmail());

        return profileRepository.save(profile);
    }

    // password 수정
    public PasswordResponseDto updatePassword(PasswordRequestDto requestDto) throws BadRequestException {
        // 현재 사용자
        Authentication loginUser =  SecurityContextHolder.getContext().getAuthentication();
        String userName = loginUser.getName();

        User user = userRepository.findByUsername(userName).orElseThrow(
                () -> new IllegalArgumentException("다시 확인해주세요")
        );

        return passwordComparison(requestDto, user);
    }


    // password 수정
    public PasswordResponseDto passwordComparison(PasswordRequestDto requestDto, User user) {
        List<PasswordHistory> passwordHistories = user.getPasswordHistories();
        String nowPassword = user.getPassword();
        int idCount = passwordHistories.size();
        log.info("idCount : " + idCount);

        validatePassword(idCount, nowPassword, requestDto, passwordHistories);

        userService.savePasswordHistory();
        userService.updatePassword(requestDto);
        return new PasswordResponseDto(requestDto);
    }

    // 최근 비밀번호와 비교함
    public void validatePassword(int idCount, String nowPassword, PasswordRequestDto requestDto, List<PasswordHistory> passwordHistories){
        if (idCount == 0) {
            if(passwordEncoder.matches(requestDto.getPassword(), nowPassword)){
                throw new IllegalArgumentException("비밀번호를 사용할 수 없습니다.");
            }
        } else if (idCount < 3) {
            for(int i=0; i<idCount; i++){
                String oldPassword = passwordHistories.get(i).getPassword();
                if(passwordEncoder.matches(requestDto.getPassword(), oldPassword) || passwordEncoder.matches(requestDto.getPassword(), nowPassword)){
                    throw new IllegalArgumentException("최근 변경된 비밀번호입니다. 새로운 비밀번호를 입력해주세요.");
                }
            }
        } else {
            for(int i=0; i<3; i++){
                String oldPassword = passwordHistories.get(idCount - 1 - i).getPassword();
                if(passwordEncoder.matches(requestDto.getPassword(), oldPassword) || passwordEncoder.matches(requestDto.getPassword(), nowPassword)){
                    throw new IllegalArgumentException("최근 변경된 비밀번호입니다. 새로운 비밀번호를 입력해주세요.");
                }
            }
        }
    }

    public ProfileGetResponseDto getProfile(UserDetails userDetails) {
        String userName = userDetails.getUsername();
        User user = userService.getUserByName(userName);
        log.error("user id"+user.getUserId());

        Profile profile = user.getProfile();
        profile.updateEmail(user.getEmail());
        profile.setCommentLikeCount();
        profile.setPostLikeCount();
        profileRepository.save(profile);

        return new ProfileGetResponseDto(profile.getEmail(), profile.getAddress(), profile.getIntroduction(), profile.getCommentLikeCount(), profile.getPostLikeCount());
    }
}
