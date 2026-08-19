package com.example.backend.repository; import com.example.backend.entity.*; import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant,ConversationParticipantId>{ List<ConversationParticipant> findByUser(User user); boolean existsByConversationAndUser(Conversation c,User u); }
