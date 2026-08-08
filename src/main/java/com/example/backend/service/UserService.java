package com.example.backend.service;

import com.example.backend.entity.User;

public interface UserService {

    User createUser(User user);

    User findByEmail(String email);

    User findByTelephone(String telephone);

    User login(String login, String password);

}