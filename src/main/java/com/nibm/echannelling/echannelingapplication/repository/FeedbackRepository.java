package com.nibm.echannelling.echannelingapplication.repository;


import com.nibm.echannelling.echannelingapplication.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;




import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByDoctorId(Long doctorId);
}