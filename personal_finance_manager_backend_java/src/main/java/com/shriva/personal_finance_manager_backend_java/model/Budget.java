package com.shriva.personal_finance_manager_backend_java.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "budgets")
@Data
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}