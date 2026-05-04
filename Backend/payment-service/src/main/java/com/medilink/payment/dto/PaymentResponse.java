package com.medilink.payment.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private UUID paymentId;
    private String razorpayOrderId;
    private String status;
}