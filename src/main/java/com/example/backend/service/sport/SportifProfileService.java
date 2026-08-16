package com.example.backend.service.sport;

import com.example.backend.dto.sportif.SportifProfileRequest;
import com.example.backend.dto.sportif.SportifProfileResponse;

public interface SportifProfileService {

    SportifProfileResponse create(
            SportifProfileRequest request,
            Long userId
    );

    SportifProfileResponse getMyProfile(
            Long userId
    );

    SportifProfileResponse update(
            SportifProfileRequest request,
            Long userId
    );

    void delete(
            Long userId
    );
}