package com.example.backend.service.profile;

import com.example.backend.dto.profile.UserProfileRequest;
import com.example.backend.dto.profile.UserProfileResponse;

public interface UserProfileService {
    UserProfileResponse getProfile(Long userId, Long viewerId);
    UserProfileResponse updateMyProfile(Long userId, UserProfileRequest request);
}
