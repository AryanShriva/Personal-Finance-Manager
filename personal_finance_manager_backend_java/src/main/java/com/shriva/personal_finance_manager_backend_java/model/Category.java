package com.shriva.personal_finance_manager_backend_java.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories", schema = "personal_finance")
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

     @ManyToOne
     @JoinColumn(name = "user_id")
     private User user;
}