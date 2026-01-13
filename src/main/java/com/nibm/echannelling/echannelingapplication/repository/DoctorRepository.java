package com.nibm.echannelling.echannelingapplication.repository;

import com.nibm.echannelling.echannelingapplication.entity.Doctor;
import com.nibm.echannelling.echannelingapplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.Query;


public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Query("SELECT d FROM Doctor d WHERE d.user.email = :email")
    Optional<Doctor> findByEmail(String email);


    Optional<Doctor> findByUser_Email(String email);

    Optional<Doctor> findByUser(User user);

    List<Doctor> findBySpecialty(String specialty);

    Optional<Doctor> findByUserEmail(String email);
}
