package com.sparta.instahub.profile.controller;


import com.sparta.instahub.profile.dto.PasswordRequestDto;
import com.sparta.instahub.profile.dto.ProfileRequestDto;
import com.sparta.instahub.profile.entity.PasswordHistory;
import com.sparta.instahub.profile.entity.Profile;
import com.sparta.instahub.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    @PutMapping("/{Id}")
    public ResponseEntity<Profile> updateProfile(@PathVariable Long Id,
                                                 @RequestBody ProfileRequestDto requestDto) {
        return ResponseEntity.ok(profileService.updateProfile(Id, requestDto));
    }

    // 비밀번호 수정
    @PutMapping("/password")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordRequestDto requestDto) throws BadRequestException {
        profileService.updatePassword(requestDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
