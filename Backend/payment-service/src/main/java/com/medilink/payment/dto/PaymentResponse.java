package com.medilink.payment.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private UUID paymentId;
    private String razorpayOrderId;
    private String status;
    private Long amount;
    private String currency;
    private String razorpayKey;
}