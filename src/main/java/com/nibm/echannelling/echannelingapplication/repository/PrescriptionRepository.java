package com.nibm.echannelling.echannelingapplication.repository;

import com.nibm.echannelling.echannelingapplication.entity.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByAppointmentId(Long appointmentId);

    @Query("SELECT p FROM Prescription p WHERE p.appointment.doctor.id = :doctorId")
    List<Prescription> findByAppointmentDoctorId(Long doctorId);

    @Query("SELECT p FROM Prescription p WHERE p.appointment.patient.user.id = :patientUserId")
    List<Prescription> findByAppointmentPatientUserId(Long patientUserId);
}