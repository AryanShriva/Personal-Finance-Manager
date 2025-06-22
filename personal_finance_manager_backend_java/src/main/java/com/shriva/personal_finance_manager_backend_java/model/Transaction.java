package com.shriva.personal_finance_manager_backend_java.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private String type; // e.g., "EXPENSE" or "INCOME"
    private LocalDate date;
    private String category;
    private String description;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters and Setters (provided by Lombok @Data)
}