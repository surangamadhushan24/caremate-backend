package com.nibm.echannelling.echannelingapplication.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private Long appointmentId;
    private BigDecimal amount;
    private String currency;
    private String returnUrl;
    private String cancelUrl;
    private String notifyUrl;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String country;
}
