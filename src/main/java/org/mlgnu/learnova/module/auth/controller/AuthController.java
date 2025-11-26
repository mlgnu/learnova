package org.mlgnu.learnova.module.auth.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.mlgnu.learnova.module.auth.dto.LoginRequest;
import org.mlgnu.learnova.module.auth.dto.LoginResponse;
import org.mlgnu.learnova.module.auth.dto.SignupRequest;
import org.mlgnu.learnova.module.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);
        return ResponseEntity.ok().body(new LoginResponse(token));
    }

    @GetMapping("/test")
    public String test(Authentication auth) {
        return "Authenticated as " + auth.getName();
    }
}
