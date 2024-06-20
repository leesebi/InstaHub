package com.sparta.instahub.profile.service;

import com.sparta.instahub.auth.service.UserService;
import com.sparta.instahub.profile.dto.ProfileRequestDto;
import com.sparta.instahub.profile.entity.Profile;
import com.sparta.instahub.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserService userService;

    public Profile updateProfile(String userId, ProfileRequestDto requestDto) {
        userService.update(userId, requestDto.getEmail(), requestDto.getUserId());

        Profile profile = profileRepository.findByUser_UserId(userId).orElseThrow(
                () -> new IllegalArgumentException("다시 확인해 주세요")
        );

        profile.updateAddress(requestDto.getAddress());
        profile.updateIntroduction(requestDto.getIntroduction());

        return profileRepository.save(profile);
    }
}
