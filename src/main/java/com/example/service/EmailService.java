package com.example.service;

import com.example.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.*;
import java.util.Base64;

@Service
public class EmailService {
    @Value("${brevo.api.key}")
    private String brevoApiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    public void sendTicketEmail(String to, byte[] pdfBytes, String filename) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.brevo.com/v3/smtp/email";

        String pdfBase64 = Base64.getEncoder().encodeToString(pdfBytes);

        Map<String, Object> body = new HashMap<>();
        Map<String, String> sender = new HashMap<>();
        sender.put("name", senderName);
        sender.put("email", senderEmail);
        body.put("sender", sender);

        List<Map<String, String>> toList = new ArrayList<>();
        Map<String, String> toMap = new HashMap<>();
        toMap.put("email", to);
        toMap.put("name", to);
        toList.add(toMap);
        body.put("to", toList);

        body.put("subject", "Your Ticket");
        body.put("htmlContent", "<html><body><p>Please find your ticket attached as a PDF with QR code.</p></body></html>");

        List<Map<String, String>> attachments = new ArrayList<>();
        Map<String, String> attachment = new HashMap<>();
        attachment.put("name", filename);
        attachment.put("content", pdfBase64);
        attachments.add(attachment);
        body.put("attachment", attachments);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("accept", "application/json");
        headers.set("api-key", brevoApiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send email via Brevo: " + response.getBody());
        }
    }
} 