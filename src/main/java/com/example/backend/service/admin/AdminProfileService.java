package com.example.backend.service.admin;

import com.example.backend.dto.admin.AdminProfileRequest;
import com.example.backend.dto.admin.AdminProfileResponse;

public interface AdminProfileService {

    AdminProfileResponse create(
            AdminProfileRequest request,
            Long userId
    );
    AdminProfileResponse getMyProfile(Long userId);

    AdminProfileResponse findByUserId(Long userId);

    AdminProfileResponse update(
            Long userId,
            AdminProfileRequest request
    );
}