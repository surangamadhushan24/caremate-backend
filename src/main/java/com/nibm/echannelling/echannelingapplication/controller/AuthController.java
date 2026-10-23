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



    public AuthController(AuthService authService) {
        this.authService = authService;


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
    public ResponseEntity<ApiResponse<Void>> changePassword(Authentication authentication,
                                                 @RequestBody ChangePasswordRequest request) {

            String email = authentication.getName();
            authService.changePassword(email, request);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>( true,"Password changed successfully", null));


    }
}