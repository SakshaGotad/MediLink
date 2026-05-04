package com.medilink.appointment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.medilink.appointment.dto.CreatePaymentRequest;
import com.medilink.appointment.dto.PaymentResponse;

@FeignClient(name = "payment-service", url = "http://localhost:8085")
public interface PaymentClient {

    @PostMapping("/payments/create")
    PaymentResponse createPayment(@RequestBody CreatePaymentRequest request);
}