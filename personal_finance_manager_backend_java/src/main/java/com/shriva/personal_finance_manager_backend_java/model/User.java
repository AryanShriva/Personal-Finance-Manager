package com.shriva.personal_finance_manager_backend_java.model;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    // Getters and setters
}
