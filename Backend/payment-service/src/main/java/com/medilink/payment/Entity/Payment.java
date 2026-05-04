package com.medilink.payment.entity;
import java.time.LocalDateTime;
import java.util.UUID;

import com.medilink.payment.enums.PaymentStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "payments")
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID appointmentId;
    private UUID patientId;

    private Long amount;


    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String razorpayOrderId;

    private LocalDateTime createdAt;
}