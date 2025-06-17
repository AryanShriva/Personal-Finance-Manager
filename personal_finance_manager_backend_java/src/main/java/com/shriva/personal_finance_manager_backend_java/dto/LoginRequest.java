package com.shriva.personal_finance_manager_backend_java.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}