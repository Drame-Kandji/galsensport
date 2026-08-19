package com.example.backend.repository;

import com.example.backend.entity.AccountToken;
import com.example.backend.entity.AccountTokenPurpose;
import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountTokenRepository extends JpaRepository<AccountToken, UUID> {
    Optional<AccountToken> findByTokenHashAndPurpose(String tokenHash, AccountTokenPurpose purpose);
    void deleteByUserAndPurpose(User user, AccountTokenPurpose purpose);
}
