package com.springproject.authservice.service;

import com.springproject.authservice.entity.UserCredential;
import com.springproject.authservice.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserCredentialRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String saveUser(UserCredential credential) {
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        repository.save(credential);
        return "User registered successfully";
    }

    public boolean userExists(String email) {
        Optional<UserCredential> userOptional = repository.findByEmail(email);
        return userOptional.isPresent();
    }


    public Integer findUserIdByEmail(String email) {
        Integer id = null;
        Optional<UserCredential> userOptional = repository.findByEmail(email);
        if (userOptional.isPresent()) {
            UserCredential userCredential = userOptional.get();
             id = userCredential.getId();
        }
        return id;
    }
    public String generateToken(Integer userId, Date expiration) {
        return jwtService.generateToken(userId,expiration);
    }



}
