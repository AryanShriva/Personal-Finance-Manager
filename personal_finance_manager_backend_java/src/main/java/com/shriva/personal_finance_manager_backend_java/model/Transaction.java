package com.shriva.personal_finance_manager_backend_java.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "transactions", schema = "personal_finance")
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    private String description;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String notes;
}