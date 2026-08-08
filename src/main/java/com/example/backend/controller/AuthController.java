package com.example.backend.controller;


import com.example.backend.dto.auth.AuthResponse;
import com.example.backend.dto.auth.LoginRequest;
import com.example.backend.dto.auth.RegisterRequest;
import com.example.backend.entity.User;
import com.example.backend.mapper.UserMapper;
import com.example.backend.service.JwtService;
import com.example.backend.service.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {



    private final UserService userService;

    private final UserMapper userMapper;

    private final JwtService jwtService;


    public AuthController(
            UserService userService,
            UserMapper userMapper,
            JwtService jwtService
    ) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }




    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request
    ){


        User user = userMapper.toUser(request);


        User savedUser = userService.createUser(user);


        AuthResponse response =
                userMapper.toAuthResponse(
                        savedUser,
                        null
                );


        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        User user = userService.login(
                request.getLogin(),
                request.getPassword()
        );

        String token =
                jwtService.generateToken(user);

        AuthResponse response =
                userMapper.toAuthResponse(
                        user,
                        token
                );

        return ResponseEntity.ok(response);
    }

}