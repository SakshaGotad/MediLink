package com.medilink.payment.service;

import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medilink.payment.dto.CreatePaymentRequest;
import com.medilink.payment.dto.PaymentResponse;
import com.medilink.payment.entity.Payment;
import com.medilink.payment.enums.PaymentStatus;
import com.medilink.payment.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key}")
    private String razorpayKey;

    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        log.info("Processing payment request for appointment: {}", request.getAppointmentId());

        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
        // 1️⃣ Check for existing payment to ensure idempotency
        return paymentRepository.findByAppointmentId(request.getAppointmentId())
                .map(existingPayment -> {
                    if (existingPayment.getStatus() == PaymentStatus.CREATED
                            || existingPayment.getStatus() == PaymentStatus.SUCCESS) {
                        return mapToResponse(existingPayment);
                    }

                    return createNewPayment(request);
                })
                .orElseGet(() -> createNewPayment(request));
    }

    private PaymentResponse createNewPayment(CreatePaymentRequest request) {
        try {
            // 2️⃣ Create Razorpay Order
            JSONObject options = new JSONObject();
            options.put("amount", request.getAmount()); // in paise
            options.put("currency", "INR");
            options.put("receipt", request.getAppointmentId().toString());

            log.debug("Calling Razorpay to create order: {}", options);
            Order order = razorpayClient.orders.create(options);
            String razorpayOrderId = order.get("id").toString();

            // 3️⃣ Save record in our DB
            Payment payment = new Payment();
            payment.setAppointmentId(request.getAppointmentId());
            payment.setPatientId(request.getPatientId());
            payment.setAmount(request.getAmount());
            payment.setStatus(PaymentStatus.CREATED);
            payment.setRazorpayOrderId(razorpayOrderId);
            payment.setCreatedAt(LocalDateTime.now());
            payment.setExpiresAt(LocalDateTime.now().plusMinutes(15));

            Payment savedPayment = paymentRepository.save(payment);
            log.info("Successfully created payment record {} and Razorpay Order {}", savedPayment.getId(),
                    razorpayOrderId);

            return mapToResponse(savedPayment);

        } catch (RazorpayException e) {
            log.error("Failed to create Razorpay order for appointment {}", request.getAppointmentId(), e);
            throw new RuntimeException("Error creating Razorpay order", e);
        }
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .razorpayOrderId(payment.getRazorpayOrderId())
                .status(payment.getStatus().name())
                .amount(payment.getAmount())
                .currency("INR")
                .razorpayKey(razorpayKey)
                .build();
    }
}