package com.nibm.echannelling.echannelingapplication.repository;


import com.nibm.echannelling.echannelingapplication.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}