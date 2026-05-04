package com.medilink.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medilink.payment.service.WebhookService;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentWebhookController {

    private final WebhookService webhookService;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestHeader("X-Razorpay-Signature") String signature,
            @RequestBody String payload) {

        webhookService.processWebhook(payload, signature);
        return ResponseEntity.ok("Webhook received");
    }
}