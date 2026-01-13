package com.nibm.echannelling.echannelingapplication.repository;


import com.nibm.echannelling.echannelingapplication.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByPayhereOrderId(String payhereOrderId);
}