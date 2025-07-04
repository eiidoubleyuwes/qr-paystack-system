package com.example.repository;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPaymentReference(String paymentReference);
    Optional<User> findByQrCodeData(String qrCodeData);
} 