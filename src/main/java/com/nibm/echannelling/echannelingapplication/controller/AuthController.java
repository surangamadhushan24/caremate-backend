package com.nibm.echannelling.echannelingapplication.controller;

import com.nibm.echannelling.echannelingapplication.config.JwtUtils;
import com.nibm.echannelling.echannelingapplication.dto.*;
import com.nibm.echannelling.echannelingapplication.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtUtils jwtUtils;


    public AuthController(AuthService authService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;

    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody RegisterRequest request) {
            authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponse<>(true,"Registration successful",null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,"Login successful",response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AccountDto>> getCurrentUser(Authentication authentication) {

        AccountDto user= authService.getCurrentUser(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,"User details fetched successfully",user)
        );

    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestHeader("Authorization") String authHeader,
                                                 @RequestBody ChangePasswordRequest request) {
        System.out.println("Change password request received");
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                System.out.println("Invalid authorization header");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authorization header");
            }
            String token = authHeader.substring(7);

            if (!jwtUtils.validateToken(token)) {
                System.out.println("Invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }

            String email = jwtUtils.getEmailFromToken(token);
            authService.changePassword(email, request);
            System.out.println("Password changed successfully for email: " + email);
            return ResponseEntity.ok("Password changed successfully");
        } catch (Exception e) {
            System.out.println("Change password failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}