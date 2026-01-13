package com.nibm.echannelling.echannelingapplication.repository;

import com.nibm.echannelling.echannelingapplication.entity.Patient;
import com.nibm.echannelling.echannelingapplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


import org.springframework.data.jpa.repository.Query;



public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Query("SELECT p FROM Patient p WHERE p.user.email = :email")
    Optional<Patient> findByEmail(String email);
    Optional<Patient> findByUser(User user);
}

