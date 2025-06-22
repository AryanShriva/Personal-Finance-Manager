package com.shriva.personal_finance_manager_backend_java.controller;

import com.shriva.personal_finance_manager_backend_java.dto.AuthenticationResponse;
import com.shriva.personal_finance_manager_backend_java.dto.LoginRequest;
import com.shriva.personal_finance_manager_backend_java.dto.ProfileRequest;
import com.shriva.personal_finance_manager_backend_java.dto.RegisterRequest;
import com.shriva.personal_finance_manager_backend_java.model.User;
import com.shriva.personal_finance_manager_backend_java.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest request) {
        try {
            userService.registerUser(request);
            Map<String, String> response = new HashMap<>();
            response.put("token", "dummy-token-for-now"); // Replace with actual JWT generation
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
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