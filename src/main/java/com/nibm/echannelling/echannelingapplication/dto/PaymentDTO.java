package com.nibm.echannelling.echannelingapplication.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Long id;
    private Long appointmentId;
    private Long userId;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String payhereOrderId;
    private String transactionId;
    private LocalDateTime createdAt;
}
