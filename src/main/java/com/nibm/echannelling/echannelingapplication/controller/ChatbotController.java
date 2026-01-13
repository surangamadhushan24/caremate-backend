package com.nibm.echannelling.echannelingapplication.controller;

import com.nibm.echannelling.echannelingapplication.dto.ChatRequest;
import com.nibm.echannelling.echannelingapplication.dto.ChatResponse;
import com.nibm.echannelling.echannelingapplication.entity.ChatHistory;
import com.nibm.echannelling.echannelingapplication.service.ChatbotService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {
    private final ChatbotService chatbotService;

    @Autowired
    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/query")
//    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<ChatResponse> handleChatQuery(@RequestBody ChatRequest chatRequest) {
        ChatResponse response = chatbotService.getChatResponse(chatRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history/{userId}")
//    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<ChatHistory>> getChatHistory(@PathVariable Long userId) {
        List<ChatHistory> history = chatbotService.getChatHistory(userId);
        return ResponseEntity.ok(history);
    }
}