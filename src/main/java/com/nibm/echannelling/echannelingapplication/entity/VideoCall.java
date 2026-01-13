package com.nibm.echannelling.echannelingapplication.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "video_calls")
public class VideoCall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_id")
    private Long appointmentId;

    @Column(name = "room_id")
    private String roomId;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}