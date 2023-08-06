package com.springproject.authservice.controller;
import com.springproject.authservice.controller.AuthController;
import com.springproject.authservice.dto.AuthRequest;
import com.springproject.authservice.entity.UserCredential;
import com.springproject.authservice.service.AuthService;
import com.springproject.authservice.utils.TestUtils;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
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

    @Mock
    private AuthenticationManager authenticationManager;

    @Autowired
    private WebApplicationContext context;


    @Test
    @Transactional
    public void testRegister_success() throws Exception {
        UserCredential user = new UserCredential();
        user.setName("test@gmail.com");
        user.setPassword("1234");

        when(authService.saveUser(any(UserCredential.class))).thenReturn("User registered successfully");
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType("application/json")
                        .content(TestUtils.asJsonString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));
    }
    @Test
    @Transactional
    public void testGetToken_forbidden() throws Exception {
        UserCredential user = new UserCredential();
        user.setName("test@gmail.com");
        user.setPassword("1234");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/token")
                        .contentType("application/json")
                        .content(TestUtils.asJsonString(user)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Transactional
    public void testInvalidEmail_error() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("invalid");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(authRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email address should be valid"));
    }

}
