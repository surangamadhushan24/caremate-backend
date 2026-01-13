package com.nibm.echannelling.echannelingapplication.repository;

import com.nibm.echannelling.echannelingapplication.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import org.springframework.data.repository.query.Param;



public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @Query("SELECT a FROM Appointment a WHERE a.patient.user.email = :email")
    List<Appointment> findByPatientEmail(@Param("email") String email);

    @Query("SELECT a FROM Appointment a WHERE a.doctor.user.email = :email")
    List<Appointment> findByDoctorEmail(@Param("email") String email);
}