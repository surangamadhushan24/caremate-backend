package com.nibm.echannelling.echannelingapplication.controller;


import com.nibm.echannelling.echannelingapplication.service.NotificationService;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    public void sendNotification(@RequestBody Long userId, @RequestBody String message, @RequestBody String type) {
        notificationService.sendNotification(userId, message, type);
    }
}