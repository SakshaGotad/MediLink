package com.medilink.appointment.dto;
import lombok.Data;

@Data
public class PaymentResponse {
    private String paymentId;
    private String razorpayOrderId;
    private String status;
}