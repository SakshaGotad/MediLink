package com.medilink.payment.Entity;
import java.time.LocalDateTime;
import com.medilink.payment.Enums.PaymentStatus;
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
    private String id;

    private String appointmentId;
    private String patientId;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String razorpayOrderId;

    private LocalDateTime createdAt;
}