package com.qypenguin.ai.controller;

import com.qypenguin.ai.domain.ApiResponse;
import com.qypenguin.ai.domain.UserProfile;
import com.qypenguin.ai.dto.ProfileUpsertRequest;
import com.qypenguin.ai.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
  private final ProfileService profileService;

  public ProfileController(ProfileService profileService) {
    this.profileService = profileService;
  }

  @GetMapping("/{userId}")
  public ApiResponse<UserProfile> get(@PathVariable String userId) {
    return ApiResponse.ok(profileService.get(userId));
  }

  @PostMapping("/{userId}")
  @PutMapping("/{userId}")
  public ApiResponse<UserProfile> upsert(@PathVariable String userId, @Valid @RequestBody ProfileUpsertRequest request) {
    return ApiResponse.ok(profileService.upsert(userId, request));
  }
}
