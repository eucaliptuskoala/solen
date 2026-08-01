package org.solen.configuration.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private static final String SECRET = "test-secret-key-that-is-at-least-32-bytes-long-for-hs256";

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(SECRET);
    }

    @Test
    void generateTokenAndExtractEmail_roundTrip() {
        String token = jwtUtil.generateToken("user@test.com", 1L, "Alice");

        String email = jwtUtil.extractEmail(token);

        assertEquals("user@test.com", email);
    }

    @Test
    void validateToken_validToken_returnsTrue() {
        String token = jwtUtil.generateToken("user@test.com", 1L, "Alice");

        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void validateToken_malformedToken_returnsFalse() {
        assertFalse(jwtUtil.validateToken("invalid-token"));
    }

    @Test
    void validateToken_emptyToken_returnsFalse() {
        assertFalse(jwtUtil.validateToken(""));
    }

    @Test
    void extractEmail_invalidToken_throws() {
        assertThrows(Exception.class, () -> jwtUtil.extractEmail("invalid-token"));
    }

    @Test
    void shortSecret_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new JwtUtil("too-short"));
    }

    @Test
    void tokensWithDifferentSecrets_doNotValidate() {
        JwtUtil other = new JwtUtil("a-different-secret-key-that-is-also-long-enough-for-hs256");
        String token = jwtUtil.generateToken("user@test.com", 1L, "Alice");

        assertFalse(other.validateToken(token));
    }
}
