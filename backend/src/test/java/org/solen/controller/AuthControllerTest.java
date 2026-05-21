package org.solen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.solen.business.exceptions.UserNotFoundByEmailException;
import org.solen.business.signincases.ISignInUseCase;
import org.solen.business.usercases.UserDetailsService;
import org.solen.configuration.GlobalExceptionHandler;
import org.solen.configuration.security.JwtUtil;
import org.solen.controller.dto.auth.SignInRequest;
import org.solen.controller.dto.auth.SignInResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ISignInUseCase signInUseCase;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void signIn_withValidCredentials_returnsToken() throws Exception {
        SignInRequest request = SignInRequest.builder()
                .email("user@test.com")
                .password("password")
                .build();
        when(signInUseCase.signIn(request))
                .thenReturn(SignInResponse.builder().token("jwt-token").build());

        mockMvc.perform(post("/auth/sign_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    void signIn_withUnknownEmail_returns404() throws Exception {
        SignInRequest request = SignInRequest.builder()
                .email("unknown@test.com")
                .password("password")
                .build();
        when(signInUseCase.signIn(request))
                .thenThrow(new UserNotFoundByEmailException("unknown@test.com"));

        mockMvc.perform(post("/auth/sign_in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
