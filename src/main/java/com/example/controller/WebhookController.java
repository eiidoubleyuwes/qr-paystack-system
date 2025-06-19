package com.example.controller;

import com.example.dto.PaystackWebhookRequest;
import com.example.service.WebhookProcessingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhook/paystack")
public class WebhookController {
    private final WebhookProcessingService webhookProcessingService;

    public WebhookController(WebhookProcessingService webhookProcessingService) {
        this.webhookProcessingService = webhookProcessingService;
    }

    @PostMapping
    public ResponseEntity<String> handlePaystackWebhook(@RequestBody PaystackWebhookRequest payload) {
        webhookProcessingService.processWebhookAsync(payload);
        return ResponseEntity.ok("Received");
    }
} 