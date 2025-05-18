package com.finance.gateway.controller;

import com.finance.gateway.model.AuthRequest;
import com.finance.gateway.model.AuthResponse;
import com.finance.gateway.security.JwtAuth;
import com.finance.gateway.util.RestAPIClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthenticationController {

    private final RestAPIClient restAPIClient;
    private final JwtAuth jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;


    @Autowired
    public AuthenticationController(RestAPIClient restAPIClient, JwtAuth jwtUtil) {
        this.restAPIClient = restAPIClient;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            log.info("Processing login request for username: {}", request.getUsername());

            final AuthRequest userDetails = restAPIClient.fetchUserDetails(request.getUsername());
            log.info("Rest api password :{} ", userDetails.getPassword());
            if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid credentials");
            }

            String jwt = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new AuthResponse(jwt));

        } catch (BadCredentialsException | UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
        }
    }
}