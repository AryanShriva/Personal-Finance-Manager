//package com.shriva.personal_finance_manager_backend_java.model;
//
//@Entity
//public class Transaction {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    private Double amount;
//    private String description;
//    private Date date;
//    private String type; // "INCOME" or "EXPENSE"
//    @ManyToOne
//    private Category category;
//    @ManyToOne
//    private User user;
//    // Getters and setters
//}