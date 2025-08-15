package com.odeal.merchant.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "merchants")
public class Merchant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(hidden = true, required = true)
    private UUID id;
    
    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    @Schema(description = "Full name of the merchant", required = true, example = "John Doe")
    private String name;
    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    @Schema(description = "Email of the merchant", required = true, example = "john.doe@example.com")
    private String email;
    
    @NotBlank(message = "IBAN is required")
    @Column(nullable = false)
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{4}[0-9]{7}([A-Z0-9]?){0,16}$", message = "Invalid IBAN format")
    @Schema(description = "IBAN of the merchant", required = true, example = "TR12345678901234567890123456")
    private String iban;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(hidden = true, required = true)
    private LocalDateTime createdAt;
    
    // Constructors
    public Merchant() {}
    
    public Merchant(String name, String email, String iban) {
        this.name = name;
        this.email = email;
        this.iban = iban;
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getIban() {
        return iban;
    }
    
    public void setIban(String iban) {
        this.iban = iban;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
