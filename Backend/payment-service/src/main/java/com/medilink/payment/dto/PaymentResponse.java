package com.medilink.payment.dto;
        
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private String paymentId;
    private String razorpayOrderId;
    private String status;
}