package com.medilink.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.medilink.payment.entity.Payment;
import com.medilink.payment.enums.PaymentStatus;
import com.medilink.payment.repository.PaymentRepository;
import com.razorpay.Utils;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

    private final PaymentRepository paymentRepository;

    @Value("${razorpay.webhook-secret}")
    private String secret;

    public void processWebhook(String payload, String signature) {

        try {
            boolean isValid = Utils.verifyWebhookSignature(payload, signature, secret);

            if (!isValid) {
                throw new RuntimeException("Invalid webhook signature");
            }

            JSONObject json = new JSONObject(payload);

            String event = json.getString("event");

            if ("payment.captured".equals(event)) {

                JSONObject paymentEntity = json
                        .getJSONObject("payload")
                        .getJSONObject("payment")
                        .getJSONObject("entity");

                String orderId = paymentEntity.getString("order_id");

                log.info("Payment captured for orderId: {}", orderId);

                Optional<Payment> optionalPayment = paymentRepository.findByRazorpayOrderId(orderId);

                Payment payment = optionalPayment.get();

                if ("payment.captured".equals(event)) {

                    payment.setStatus(PaymentStatus.SUCCESS);
                    paymentRepository.save(payment);

                    log.info("Payment SUCCESS for orderId: {}", orderId);
                } else if ("payment.failed".equals(event)) {

                    payment.setStatus(PaymentStatus.FAILED);
                    paymentRepository.save(payment);

                    log.info("Payment FAILED for orderId: {}", orderId);
                }
            }

        } catch (Exception e) {
            log.error("Webhook processing failed: {}", e.getMessage());
            throw new RuntimeException("Webhook error");
        }
    }
}