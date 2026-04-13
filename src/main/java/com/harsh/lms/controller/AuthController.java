package com.harsh.lms.controller;

import com.harsh.lms.dto.LoginRequest;
import com.harsh.lms.dto.LoginResponse;
import com.harsh.lms.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginCredentials) {
        LoginResponse loginResponse = authService.authenticateUser(loginCredentials.getUsername(), loginCredentials.getPassword());
        return loginResponse;
    }
}
