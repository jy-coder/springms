package com.springproject.authservice.controller;
import com.springproject.authservice.BaseTest;
import com.springproject.authservice.dto.AuthRequest;
import com.springproject.authservice.entity.UserCredential;
import com.springproject.authservice.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest  {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    public void testRegister() throws Exception {
        UserCredential user = new UserCredential();
        user.setName("testUser");
        user.setPassword("testPassword");

        when(authService.saveUser(any(UserCredential.class))).thenReturn("User registered successfully");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType("application/json")
                        .content(asJsonString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));
    }
    @Test
    @DependsOn("testRegister")
    public void testGetToken() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("testUser");
        authRequest.setPassword("testPassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(authRequest)))
                .andExpect(status().isOk());
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
