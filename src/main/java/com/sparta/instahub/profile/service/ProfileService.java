package com.sparta.instahub.profile.service;

import com.sparta.instahub.auth.entity.User;
import com.sparta.instahub.auth.service.UserService;
import com.sparta.instahub.profile.dto.PasswordRequestDto;
import com.sparta.instahub.profile.dto.ProfileRequestDto;
import com.sparta.instahub.profile.entity.PasswordHistory;
import com.sparta.instahub.profile.entity.Profile;
import com.sparta.instahub.profile.repository.PasswordHistoryRepository;
import com.sparta.instahub.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
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
    public void updatePassword(PasswordRequestDto requestDto) throws BadRequestException {
        User user = userService.savePasswordHistory();

        passwordComparison(requestDto, user);
    }


    // password 비교
    public void passwordComparison(PasswordRequestDto requestDto, User user) {
        List<PasswordHistory> passwordHistories = user.getPasswordHistories();
        int idCount = passwordHistories.size();
        if(idCount > 4){
            for(int i = 1; i<3; i++) {
                String oldPassword = passwordHistories.get(idCount - i).getPassword();

                if(passwordEncoder.matches(requestDto.getPassword(), oldPassword)){
                    throw new IllegalArgumentException("사용할 수 없는 비밀번호입니다.");
                }else{
                    userService.updatePassword(requestDto);
                }
            }
        }
        else {
            for(int i = 0; i<=idCount; i++) {
                int index = idCount - i - 1;
                if (index < 0 || index >= passwordHistories.size()) {
                    continue;
                }

                String oldPassword = passwordHistories.get(index).getPassword();

                if(passwordEncoder.matches(requestDto.getPassword(), oldPassword)){
                    throw new IllegalArgumentException("사용할 수 없는 비밀번호입니다.");
                }else{
                    userService.updatePassword(requestDto);
                }
            }
        }

    }
}
