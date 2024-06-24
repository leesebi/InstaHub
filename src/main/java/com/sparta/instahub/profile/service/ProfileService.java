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

        Long idCount = passwordHistoryRepository.countBy();

        passwordComparison(idCount, requestDto, user);
    }


    // password 비교
    public void passwordComparison(Long idCount, PasswordRequestDto requestDto, User user) throws BadRequestException{
        List<PasswordHistory> passwordHistories = user.getPasswordHistories();

        if(idCount > 4){
            for(long i = 1L; i<3; i++) {
                int index = (int) (idCount - i - 1);
                if (index < 0 || index >= passwordHistories.size()) {
                    continue;
                }
                String oldPassword = passwordHistories.get((int) (idCount - i)).getPassword();
                if(passwordEncoder.matches(oldPassword, requestDto.getPassword())){
                    throw new BadRequestException("사용할 수 없는 비밀번호입니다.");
                }
            }
        }else {
            for(long i = 1L; i<=idCount; i++) {
                int index = (int) (idCount - i - 1);
                if (index < 0 || index >= passwordHistories.size()) {
                    continue;
                }
                String oldPassword = passwordHistories.get(Math.toIntExact(i)).getPassword();

                if(passwordEncoder.matches(oldPassword, requestDto.getPassword())){
                    throw new BadRequestException("사용할 수 없는 비밀번호입니다.");
                }
            }
        }
        userService.updatePassword(requestDto);
    }
}
