package com.example.controller;

import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.QRCodeService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserRepository userRepository;
    private final QRCodeService qrCodeService;

    public AdminController(UserRepository userRepository, QRCodeService qrCodeService) {
        this.userRepository = userRepository;
        this.qrCodeService = qrCodeService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "admin-dashboard";
    }

    @GetMapping("/api/stats")
    @ResponseBody
    public Map<String, Long> stats() {
        long total = userRepository.count();
        long scanned = userRepository.findAll().stream().filter(User::isAttended).count();
        return Map.of("total", total, "scanned", scanned);
    }

    @GetMapping("/api/tickets")
    @ResponseBody
    public List<User> tickets() {
        return userRepository.findAll();
    }

    @GetMapping("/api/qr/{id}")
    public ResponseEntity<ByteArrayResource> getQr(@PathVariable Long id) throws IOException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        try {
            byte[] qr = qrCodeService.generateQRCode(user.getQrCodeData(), 300, 300);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(new ByteArrayResource(qr));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
} 