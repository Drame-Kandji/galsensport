package com.example.backend.repository; import com.example.backend.entity.Conversation; import org.springframework.data.jpa.repository.*;
public interface ConversationRepository extends JpaRepository<Conversation,Long>{}
