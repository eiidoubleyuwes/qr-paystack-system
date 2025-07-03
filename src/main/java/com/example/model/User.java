package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty("id")
    private Long id;
    @Column(name = "first_name")
    @JsonProperty("firstName")
    private String firstName;
    @Column(name = "last_name")
    @JsonProperty("lastName")
    private String lastName;
    @Column(name = "email")
    @JsonProperty("email")
    private String email;
    @Column(name = "phone")
    @JsonProperty("phone")
    private String phone;
    @Column(name = "payment_reference")
    @JsonProperty("paymentReference")
    private String paymentReference;
    @Column(name = "qr_code_data")
    @JsonProperty("qrCodeData")
    private String qrCodeData;
    @Column(name = "attended")
    @JsonProperty("attended")
    private boolean attended;
    @Column(name = "created_at")
    @JsonProperty("createdAt")
    private String createdAt;

    public User() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPaymentReference() { return paymentReference; }
    public void setPaymentReference(String paymentReference) { this.paymentReference = paymentReference; }
    public String getQrCodeData() { return qrCodeData; }
    public void setQrCodeData(String qrCodeData) { this.qrCodeData = qrCodeData; }
    public boolean isAttended() { return attended; }
    public void setAttended(boolean attended) { this.attended = attended; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
} 