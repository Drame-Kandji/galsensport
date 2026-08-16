package com.example.backend.service.jwt;

import com.example.backend.entity.User;

public interface JwtService {

    String generateToken(User user);

}