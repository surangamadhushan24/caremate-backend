package com.nibm.echannelling.echannelingapplication.service;

import com.nibm.echannelling.echannelingapplication.dto.PaymentDTO;
import com.nibm.echannelling.echannelingapplication.dto.PaymentRequest;
import com.nibm.echannelling.echannelingapplication.dto.PaymentResponse;
import com.nibm.echannelling.echannelingapplication.entity.Appointment;
import com.nibm.echannelling.echannelingapplication.entity.Payment;
import com.nibm.echannelling.echannelingapplication.entity.User;
import com.nibm.echannelling.echannelingapplication.repository.AppointmentRepository;
import com.nibm.echannelling.echannelingapplication.repository.PaymentRepository;
import com.nibm.echannelling.echannelingapplication.repository.UserRepository;
import org.springframework.stereotype.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.transaction.annotation.Transactional;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {


    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @Value("${payhere.merchant.id}")
    private String merchantId;

    @Value("${payhere.merchant.secret}")
    private String merchantSecret;

    @Value("${payhere.base.url}")
    private String payhereBaseUrl;

    public PaymentService(PaymentRepository paymentRepository, AppointmentRepository appointmentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PaymentResponse initiatePayment(PaymentRequest request) {
        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String orderId = UUID.randomUUID().toString();
        Payment payment = new Payment();
        payment.setAppointment(appointment);
        payment.setUser(user);
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setStatus("PENDING");
        payment.setPayhereOrderId(orderId);
        paymentRepository.save(payment);

        String hash = generatePayhereHash(
                merchantId,
                orderId,
                request.getAmount().toString(),
                request.getCurrency(),
                merchantSecret
        );

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("merchant_id", merchantId);
        paymentData.put("return_url", request.getReturnUrl());
        paymentData.put("cancel_url", request.getCancelUrl());
        paymentData.put("notify_url", request.getNotifyUrl());
        paymentData.put("order_id", orderId);
        paymentData.put("items", "Appointment Payment");
        paymentData.put("currency", request.getCurrency());
        paymentData.put("amount", request.getAmount().toString());
        paymentData.put("first_name", request.getFirstName());
        paymentData.put("last_name", request.getLastName());
        paymentData.put("email", request.getEmail());
        paymentData.put("phone", request.getPhone());
        paymentData.put("address", request.getAddress());
        paymentData.put("city", request.getCity());
        paymentData.put("country", request.getCountry());
        paymentData.put("hash", hash);

        String paymentUrl = payhereBaseUrl + "/pay/checkout";

        PaymentResponse response = new PaymentResponse();
        response.setPaymentUrl(paymentUrl);
        response.setPayhereOrderId(orderId);
        response.setStatus("INITIATED");
        return response;
    }

    @Transactional
    public void handlePaymentNotification(Map<String, String> notification) {
        String orderId = notification.get("order_id");
        String statusCode = notification.get("status_code");
        String md5sig = notification.get("md5sig");

        Payment payment = paymentRepository.findByPayhereOrderId(orderId);
        if (payment == null) {
            throw new RuntimeException("Payment not found");
        }

        // Verify PayHere signature
        String localMd5sig = generatePayhereNotificationHash(
                notification.get("merchant_id"),
                orderId,
                notification.get("payhere_amount"),
                notification.get("payhere_currency"),
                statusCode,
                merchantSecret
        );

        if (!localMd5sig.equalsIgnoreCase(md5sig)) {
            throw new RuntimeException("Invalid signature");
        }

        if ("2".equals(statusCode)) {
            payment.setStatus("COMPLETED");
            payment.setTransactionId(notification.get("payment_id"));
        } else if ("0".equals(statusCode)) {
            payment.setStatus("PENDING");
        } else if ("-1".equals(statusCode)) {
            payment.setStatus("CANCELLED");
        } else if ("-2".equals(statusCode)) {
            payment.setStatus("FAILED");
        } else if ("-3".equals(statusCode)) {
            payment.setStatus("CHARGEDBACK");
        }

        paymentRepository.save(payment);
    }

    private String generatePayhereHash(String merchantId, String orderId, String amount, String currency, String merchantSecret) {
        String hashInput = merchantId + orderId + amount + currency + merchantSecret;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(hashInput.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    private String generatePayhereNotificationHash(String merchantId, String orderId, String amount, String currency, String statusCode, String merchantSecret) {
        String hashInput = merchantId + orderId + amount + currency + statusCode + merchantSecret.toUpperCase();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(hashInput.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString().toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }
    }

    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setAppointmentId(payment.getAppointment().getId());
        dto.setUserId(payment.getUser().getId());
        dto.setAmount(payment.getAmount());
        dto.setCurrency(payment.getCurrency());
        dto.setStatus(payment.getStatus());
        dto.setPayhereOrderId(payment.getPayhereOrderId());
        dto.setTransactionId(payment.getTransactionId());
        dto.setCreatedAt(payment.getCreatedAt());
        return dto;
    }
}