package com.nibm.echannelling.echannelingapplication.controller;

import com.nibm.echannelling.echannelingapplication.dto.PaymentDTO;
import com.nibm.echannelling.echannelingapplication.dto.PaymentRequest;
import com.nibm.echannelling.echannelingapplication.dto.PaymentResponse;
import com.nibm.echannelling.echannelingapplication.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

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

    /**
     * PayHere calls this endpoint server-to-server with form-urlencoded data (NOT JSON).
     * Must be publicly accessible (permitAll) and reachable from the internet.
     */
    @PostMapping(value = "/notify", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> handlePaymentNotification(@RequestParam Map<String, String> notification) {
        try {
            paymentService.handlePaymentNotification(notification);
        } catch (Exception e) {
            // Log but still return 200 so PayHere doesn't keep retrying on our internal errors
            log.error("Failed to process PayHere notification: {}", e.getMessage(), e);
        }
        return ResponseEntity.ok("Notification received");
    }


    @GetMapping("/order/{orderId}/status")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> getPaymentStatusByOrderId(@PathVariable String orderId,
                                                                         Authentication authentication) {
        String status = paymentService.getPaymentStatusByOrderId(orderId, authentication.getName());
        return ResponseEntity.ok(Map.of("status", status));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PATIENT') or hasRole('ADMIN')")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        PaymentDTO payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }
}