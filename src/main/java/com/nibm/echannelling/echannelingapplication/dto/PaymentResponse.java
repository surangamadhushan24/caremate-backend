package com.nibm.echannelling.echannelingapplication.dto;

import lombok.Data;


@Data
public class PaymentResponse {
    private String paymentUrl;
    private String payhereOrderId;
    private String status;
}