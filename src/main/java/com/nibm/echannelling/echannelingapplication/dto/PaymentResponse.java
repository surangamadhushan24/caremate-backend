package com.nibm.echannelling.echannelingapplication.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PaymentResponse {

    private String paymentUrl;
    private String payhereOrderId;
    private String status;
    private Map<String, String> paymentData;


}