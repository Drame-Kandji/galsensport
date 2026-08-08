package com.example.backend.dto.auth;


import jakarta.validation.constraints.NotBlank;


public class LoginRequest {


    @NotBlank(message = "Email ou téléphone obligatoire")
    private String login;


    @NotBlank(message = "Mot de passe obligatoire")
    private String password;



    public LoginRequest() {
    }



    public LoginRequest(
            String login,
            String password
    ) {
        this.login = login;
        this.password = password;
    }



    public String getLogin() {
        return login;
    }



    public String getPassword() {
        return password;
    }
}