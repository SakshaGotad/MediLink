package com.medilink.appointment.dto;
import lombok.Data;

@Data
public class CreatePaymentRequest {
    private String appointmentId;
    private String patientId;
    private Long amount;
}