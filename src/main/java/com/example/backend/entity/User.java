package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_users_email",
                        columnNames = "email"
                ),
                @UniqueConstraint(
                        name = "uk_users_telephone",
                        columnNames = "telephone"
                )
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 150
    )
    private String email;

    @Column(
            nullable = false,
            unique = true,
            length = 20
    )
    private String telephone;

    @Column(
            nullable = false
    )
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private Role role;


    public User() {
    }


    public User(
            String email,
            String telephone,
            String password,
            Role role
    ) {
        this.email = email;
        this.telephone = telephone;
        this.password = password;
        this.role = role;
    }


    public Long getId() {
        return id;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public String getTelephone() {
        return telephone;
    }


    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public Role getRole() {
        return role;
    }


    public void setRole(Role role) {
        this.role = role;
    }
}