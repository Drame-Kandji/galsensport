package com.example.backend.entity;
import jakarta.persistence.*;
@Entity @Table(name="conversation_participants") @IdClass(ConversationParticipantId.class)
public class ConversationParticipant { @Id @ManyToOne @JoinColumn(name="conversation_id") private Conversation conversation; @Id @ManyToOne @JoinColumn(name="user_id") private User user; public ConversationParticipant(){} public ConversationParticipant(Conversation c,User u){conversation=c;user=u;} public Conversation getConversation(){return conversation;} public User getUser(){return user;} }
