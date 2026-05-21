package org.solen.configuration.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityConfigTest {

    @MockitoBean
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void securityFilterChain_beanExists() {
        assertNotNull(securityFilterChain);
    }

    @Test
    void passwordEncoder_beanExists() {
        assertNotNull(passwordEncoder);
    }

    @Test
    void corsConfigurationSource_isConfigured() {
        assertNotNull(securityConfig.corsConfigurationSource());
    }
}
