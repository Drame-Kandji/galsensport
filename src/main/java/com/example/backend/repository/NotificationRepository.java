package com.example.backend.repository;
import com.example.backend.entity.Notification; import com.example.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository; import org.springframework.data.domain.*;
public interface NotificationRepository extends JpaRepository<Notification,Long>{ Page<Notification> findByRecipientOrderByCreatedAtDesc(User recipient, Pageable page); long countByRecipientAndReadAtIsNull(User recipient); }
