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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final UserService userService;

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
        PasswordHistory passwordHistory = userService.savePasswordHistory();

        Long id = passwordHistory.getId();

        passwordComparison(id, requestDto);
    }


    // password 비교
    public void passwordComparison(Long id, PasswordRequestDto requestDto) throws BadRequestException{

        if(id > 4){
            for(int i=0; i<3; i++) {
                PasswordHistory password = passwordHistoryRepository.findById(id - i).orElseThrow(
                        () -> new IllegalArgumentException("다시 확인해 주세요")
                );

                String nowPassword = password.getPassword();

                if(nowPassword.equals(requestDto.getPassword())){
                    throw new BadRequestException("사용할 수 없는 비밀번호입니다.");
                }
                else{
                    userService.updatePassword(requestDto);
                }
            }
        }else {
            for(Long i = 0L; i<id; i++) {
                PasswordHistory password = passwordHistoryRepository.findById(i).orElseThrow(
                        () -> new IllegalArgumentException("다시 확인해 주세요")
                );
                String nowPassword = password.getPassword();

                if(nowPassword.equals(requestDto.getPassword())){
                    throw new BadRequestException("사용할 수 없는 비밀번호입니다.");
                }else{
                    userService.updatePassword(requestDto);
                }
            }
        }
    }
}
