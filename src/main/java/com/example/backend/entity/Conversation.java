package com.example.backend.entity; import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="conversations") public class Conversation { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @Column(nullable=false) private LocalDateTime createdAt; @PrePersist void create(){createdAt=LocalDateTime.now();} public Long getId(){return id;} }
