package com.shriva.personal_finance_manager_backend_java.controller;

import com.shriva.personal_finance_manager_backend_java.dto.AuthenticationResponse;
import com.shriva.personal_finance_manager_backend_java.dto.LoginRequest;
import com.shriva.personal_finance_manager_backend_java.dto.ProfileRequest;
import com.shriva.personal_finance_manager_backend_java.dto.RegisterRequest;
import com.shriva.personal_finance_manager_backend_java.model.User;
import com.shriva.personal_finance_manager_backend_java.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        String token = userService.registerUser(request);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(token);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        String token = userService.loginUser(request);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.getUserProfile(username);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<User> updateProfile(@RequestBody ProfileRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User updatedUser = userService.updateUserProfile(username, request);
        return ResponseEntity.ok(updatedUser);
    }
}