package com.example.backend.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name = "notifications")
public class Notification {
 @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) @JoinColumn(name="recipient_id") private User recipient;
 @ManyToOne @JoinColumn(name="actor_id") private User actor;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private NotificationType type;
 private Long resourceId; @Column(nullable=false) private String message; private LocalDateTime readAt;
 @Column(nullable=false) private LocalDateTime createdAt;
 @PrePersist void created(){createdAt=LocalDateTime.now();}
 public Long getId(){return id;} public User getRecipient(){return recipient;} public User getActor(){return actor;} public NotificationType getType(){return type;} public Long getResourceId(){return resourceId;} public String getMessage(){return message;} public LocalDateTime getReadAt(){return readAt;} public LocalDateTime getCreatedAt(){return createdAt;} public void setReadAt(LocalDateTime value){readAt=value;}
}
