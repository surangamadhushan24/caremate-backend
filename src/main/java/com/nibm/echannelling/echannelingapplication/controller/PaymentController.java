package com.nibm.echannelling.echannelingapplication.controller;


import com.nibm.echannelling.echannelingapplication.dto.PaymentDTO;
import com.nibm.echannelling.echannelingapplication.dto.PaymentRequest;
import com.nibm.echannelling.echannelingapplication.dto.PaymentResponse;
import com.nibm.echannelling.echannelingapplication.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;


import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {


    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/initiate")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<PaymentResponse> initiatePayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.initiatePayment(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/notify")
    public ResponseEntity<String> handlePaymentNotification(@RequestBody Map<String, String> notification) {
        paymentService.handlePaymentNotification(notification);
        return ResponseEntity.ok("Notification received");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        PaymentDTO payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }
}