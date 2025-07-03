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
import java.util.stream.Collectors;

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
    public List<UserDto> tickets() {
        return userRepository.findAll().stream().filter(u -> u != null).map(UserDto::fromUser).collect(Collectors.toList());
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

    public static class UserDto {
        public Long id;
        public String firstName;
        public String lastName;
        public String email;
        public String phone;
        public String paymentReference;
        public String qrCodeData;
        public boolean attended;
        public String createdAt;
        public static UserDto fromUser(com.example.model.User u) {
            UserDto d = new UserDto();
            d.id = u.getId();
            d.firstName = u.getFirstName();
            d.lastName = u.getLastName();
            d.email = u.getEmail();
            d.phone = u.getPhone();
            d.paymentReference = u.getPaymentReference();
            d.qrCodeData = u.getQrCodeData();
            d.attended = u.isAttended();
            d.createdAt = u.getCreatedAt();
            return d;
        }
    }
} 