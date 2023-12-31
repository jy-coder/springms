package com.springproject.authservice.controller;

import com.springproject.authservice.dto.AuthRequest;
import com.springproject.authservice.dto.TokenResponse;
import com.springproject.authservice.entity.UserCredential;
import com.springproject.authservice.service.AuthService;
import com.springproject.utils.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> addNewUser(@RequestBody UserCredential user) {
        if (authService.userExists(user.getEmail())) {
            return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
        }
        String result = authService.saveUser(user);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> getToken(@RequestBody @Valid AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 30);
            Integer userId = authService.findUserIdByEmail(authRequest.getEmail());
            String token = authService.generateToken(userId,expiration);
            TokenResponse tokenResponse = new TokenResponse(token, expiration);
            return ResponseEntity.ok(tokenResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        jwtUtil.validateToken(token);
        return "Token is valid";
    }
}
