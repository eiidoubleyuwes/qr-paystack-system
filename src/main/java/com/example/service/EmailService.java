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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final String senderEmail;

    public EmailService(JavaMailSender mailSender, @Value("${spring.mail.username}") String senderEmail) {
        this.mailSender = mailSender;
        this.senderEmail = senderEmail;
    }

    public void sendTicketEmail(String to, byte[] pdfBytes, String filename, String firstName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(senderEmail);
            helper.setTo(to);
            helper.setSubject("🎉 Your Fearless Camp Ticket is Here!");
            String html = """
                <html><body>
                <h2 style='color:#0066cc;'>Hey! 👋</h2>
                <p style='font-size:1.1em;'>
                    <b>Welcome to The Fearless Movement!</b><br><br>
                    The Fearless Movement began with a vision to empower and inspire young people to live boldly in their faith and purpose. What started as a small gathering of passionate individuals has grown into a thriving community committed to mentorship, faith-based events, and creative expression.<br><br>
                    <b>You are now part of a generation that breaks barriers, spreads hope, and leads with faith and purpose.</b><br><br>
                    <span style='color:#009933;font-weight:bold;'>Your adventure at Fearless Camp awaits!</span> Attached is your official ticket.<br>
                    <ul>
                        <li>Bring this ticket with you (on your phone or printed).</li>
                        <li>Scan the QR code at the entrance to check in and start your journey!</li>
                    </ul>
                    <br>
                    Get ready for games, worship, new friends, and unforgettable moments. Through conferences, workshops, worship nights, and social impact initiatives, The Fearless Movement provides you with the tools and support to navigate life's challenges while staying rooted in your Christian faith.<br><br>
                    <i>See you at Fearless — where faith is fun, you belong, and your God-given potential is celebrated!</i>
                </p>
                <p style='font-size:0.95em;color:#888;'>If you have any questions, reply to this email. God bless!</p>
                </body></html>
            """;
            helper.setText(html, true);
            helper.addAttachment(filename, new ByteArrayResource(pdfBytes));
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email via SMTP: " + e.getMessage(), e);
        }
    }
} 