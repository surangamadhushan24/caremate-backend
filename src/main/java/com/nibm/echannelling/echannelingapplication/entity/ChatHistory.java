package com.nibm.echannelling.echannelingapplication.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "chat_history")
@Data
public class ChatHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(nullable = false, length = 2500)
    private String message;

    @Column(nullable = false, length = 2500)
    private String response;

    @CreationTimestamp
    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;
}