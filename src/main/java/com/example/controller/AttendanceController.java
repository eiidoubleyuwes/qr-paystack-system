package com.example.controller;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AttendanceController {
    private final UserRepository userRepository;

    public AttendanceController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/attend")
    public String attendPage() {
        return "attend";
    }

    @PostMapping(value = "/api/attend", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseEntity<String> markAttended(@RequestParam String code) {
        Optional<User> userOpt = userRepository.findByQrCodeData(code);
        if (userOpt.isEmpty()) {
            userOpt = userRepository.findByPaymentReference(code);
        }
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.isAttended()) {
                return ResponseEntity.ok("Already attended");
            }
            user.setAttended(true);
            userRepository.save(user);
            return ResponseEntity.ok("Attendance marked for " + user.getFirstName() + " " + user.getLastName());
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }
} 