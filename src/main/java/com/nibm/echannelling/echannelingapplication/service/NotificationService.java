package com.nibm.echannelling.echannelingapplication.service;



import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void sendNotification(Long userId, String message, String type) {
        // Integrate with SendGrid/Twilio
    }
}