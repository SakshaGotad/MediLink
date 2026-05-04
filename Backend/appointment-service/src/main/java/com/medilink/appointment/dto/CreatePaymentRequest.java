package com.medilink.appointment.dto;
import java.util.UUID;
import lombok.Data;

@Data
public class CreatePaymentRequest {
    private UUID appointmentId;
    private UUID patientId;
    private Long amount;
}