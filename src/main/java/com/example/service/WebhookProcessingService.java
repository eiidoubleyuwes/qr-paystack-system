package com.example.service;

import com.example.dto.PaystackWebhookRequest;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.google.zxing.WriterException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class WebhookProcessingService {
    private final UserRepository userRepository;
    private final QRCodeService qrCodeService;
    private final PdfService pdfService;
    private final EmailService emailService;

    public WebhookProcessingService(UserRepository userRepository, QRCodeService qrCodeService, PdfService pdfService, EmailService emailService) {
        this.userRepository = userRepository;
        this.qrCodeService = qrCodeService;
        this.pdfService = pdfService;
        this.emailService = emailService;
    }

    @Async
    public void processWebhookAsync(PaystackWebhookRequest payload) {
        if (!"charge.success".equals(payload.getEvent())) {
            return;
        }
        PaystackWebhookRequest.WebhookData data = payload.getData();
        PaystackWebhookRequest.WebhookCustomer customer = data.getCustomer();
        String reference = data.getReference();
        String email = customer.getEmail();
        String firstName = customer.getFirst_name();
        String lastName = customer.getLast_name();
        String phone = customer.getPhone();
        String qrData = reference;
        try {
            User user = User.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .phone(phone)
                    .paymentReference(reference)
                    .qrCodeData(qrData)
                    .attended(false)
                    .createdAt(LocalDateTime.now())
                    .build();
            userRepository.save(user);
            byte[] qrBytes = qrCodeService.generateQRCode(qrData, 300, 300);
            byte[] pdfBytes = pdfService.generateTicketPdf(user, qrBytes);
            try {
                emailService.sendTicketEmail(email, pdfBytes, "ticket.pdf");
            } catch (RuntimeException e) {
                System.err.println("Email sending failed: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (WriterException | IOException e) {
            System.err.println("QR/PDF generation failed: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 