package com.nibm.echannelling.echannelingapplication.repository;


import com.nibm.echannelling.echannelingapplication.entity.VideoCall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoCallRepository extends JpaRepository<VideoCall, Long> {
    Optional<VideoCall> findByAppointmentId(Long appointmentId);
}
