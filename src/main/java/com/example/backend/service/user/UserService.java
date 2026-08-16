package com.example.backend.service.user;

import com.example.backend.dto.auth.RegisterCompanyRequest;
import com.example.backend.dto.auth.RegisterUserRequest;
import com.example.backend.dto.user.UserResponse;
import com.example.backend.entity.User;

public interface UserService {

    User createUser(RegisterUserRequest request);

    User createCompany(RegisterCompanyRequest request);

    User findByEmail(String email);

    User findByTelephone(String telephone);

    User login(String login, String password);

    UserResponse getCurrentUserResponse(Long userId);
}