package com.medilink.payment.service;

import java.time.LocalDateTime;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.medilink.payment.dto.CreatePaymentRequest;
import com.medilink.payment.dto.PaymentResponse;
import com.medilink.payment.entity.Payment;
import com.medilink.payment.enums.PaymentStatus;
import com.medilink.payment.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RazorpayClient razorpayClient;

    public PaymentResponse createPayment(CreatePaymentRequest request) {

        try {
            // 1️⃣ Create Razorpay Order
            JSONObject options = new JSONObject();
            options.put("amount", request.getAmount()); // in paise
            options.put("currency", "INR");
            options.put("receipt", request.getAppointmentId());

            Order order = razorpayClient.orders.create(options);

            // 2️⃣ Save in DB
            Payment payment = new Payment();
            payment.setAppointmentId(request.getAppointmentId());
            payment.setPatientId(request.getPatientId());
            payment.setAmount(request.getAmount());
            payment.setStatus(PaymentStatus.CREATED);
            payment.setRazorpayOrderId(order.get("id"));
            payment.setCreatedAt(LocalDateTime.now());

            paymentRepository.save(payment);

            // 3️⃣ Return response
            return new PaymentResponse(
                payment.getId(),
                order.get("id"),
                payment.getStatus().name()
            );

        } catch (RazorpayException e) {
            throw new RuntimeException("Error creating Razorpay order", e);
        }
    }
}