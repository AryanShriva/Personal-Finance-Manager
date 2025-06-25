package com.shriva.personal_finance_manager_backend_java.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Amount is required")
    private Double amount;

    private String type;
    private LocalDate date;
    private String category;
    private String description;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}