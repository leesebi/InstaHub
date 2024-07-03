package com.sparta.instahub.profile.controller;


import com.sparta.instahub.profile.dto.*;
import com.sparta.instahub.profile.entity.PasswordHistory;
import com.sparta.instahub.profile.entity.Profile;
import com.sparta.instahub.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    @PutMapping("/{Id}")
    public ResponseEntity<ProfileResponseDto> updateProfile(@PathVariable Long Id,
                                                            @RequestBody ProfileRequestDto requestDto) {

        Profile updatedProfile = profileService.updateProfile(Id, requestDto);
        ProfileResponseDto responseDto = new ProfileResponseDto(
                updatedProfile.getEmail(),
                updatedProfile.getAddress(),
                updatedProfile.getIntroduction(),
                "수정이 성공적으로되었습니다."
        );

        return ResponseEntity.ok(responseDto);
    }

    // 비밀번호 수정
    @PutMapping("/password")
    public ResponseEntity<PasswordResponseDto> updatePassword(@RequestBody PasswordRequestDto requestDto) throws BadRequestException {

        return ResponseEntity.ok(profileService.updatePassword(requestDto));
    }

    //프로필 조회
    @GetMapping
    public ResponseEntity<ProfileGetResponseDto> getProfile(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(profileService.getProfile(userDetails));
    }

}
