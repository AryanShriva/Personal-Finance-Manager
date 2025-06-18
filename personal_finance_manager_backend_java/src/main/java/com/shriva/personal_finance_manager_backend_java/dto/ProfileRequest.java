package com.shriva.personal_finance_manager_backend_java.dto;

import lombok.Data;

@Data
public class ProfileRequest {
    private String email;
    private String password;
}