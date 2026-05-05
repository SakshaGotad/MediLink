package com.medilink.payment.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CreatePaymentRequest {
    private UUID appointmentId;
    private UUID patientId;
    private Long amount;
}