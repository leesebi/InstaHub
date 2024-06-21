package com.sparta.instahub.profile.controller;


import com.sparta.instahub.profile.dto.ProfileRequestDto;
import com.sparta.instahub.profile.entity.Profile;
import com.sparta.instahub.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    @PutMapping("/{userId}")
    public ResponseEntity<Profile> updateProfile(@PathVariable String userId,
                                                 @RequestBody ProfileRequestDto requestDto){
        return ResponseEntity.ok(profileService.updateProfile(userId, requestDto));
    }


}
