package com.shriva.personal_finance_manager_backend_java.service;

import com.shriva.personal_finance_manager_backend_java.dto.LoginRequest;
import com.shriva.personal_finance_manager_backend_java.dto.ProfileRequest;
import com.shriva.personal_finance_manager_backend_java.dto.RegisterRequest;
import com.shriva.personal_finance_manager_backend_java.model.Role;
import com.shriva.personal_finance_manager_backend_java.model.User;
import com.shriva.personal_finance_manager_backend_java.repository.RoleRepository;
import com.shriva.personal_finance_manager_backend_java.repository.UserRepository;
import com.shriva.personal_finance_manager_backend_java.util.JwtUtil;
import com.shriva.personal_finance_manager_backend_java.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.BadCredentialsException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public User registerUser(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());

        Role defaultRole = roleRepository.findByName("USER")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("USER");
                    return roleRepository.save(role);
                });
        user.setRole(defaultRole);

        return userRepository.save(user);
    }

    public String loginUser(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            return jwtUtil.generateToken(user.getUsername(), user.getRole().getName());
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    public User getUserProfile(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    public User updateUserProfile(String username, ProfileRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            if (userRepository.existsByEmail(request.getEmail()) && !user.getEmail().equals(request.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return userRepository.save(user);
    }
}