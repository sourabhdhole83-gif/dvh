package com.dvh.auth.service;

import com.dvh.user.constants.Role;
import com.dvh.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private String secret;
    private JwtService jwtService;
    private User user;

    @BeforeEach
    void setUp() {
        secret = Encoders.BASE64.encode(Jwts.SIG.HS256.key().build().getEncoded());
        jwtService = new JwtService(secret, 15);
        user = User.builder().id(1L).email("test@mail.com").role(Role.CUSTOMER).build();
    }

    @Test
    void shouldGenerateTokenWithUserClaims() {
        String token = jwtService.generateAccessToken(user);

        Claims claims = jwtService.parseToken(token);

        assertEquals("1", claims.getSubject());
        assertEquals("test@mail.com", claims.get("email", String.class));
        assertEquals("CUSTOMER", claims.get("role", String.class));
    }

    @Test
    void shouldRejectTamperedToken() {
        String token = jwtService.generateAccessToken(user);
        String tampered = token.substring(0, token.length() - 4) + "xxxx";

        assertFalse(jwtService.isValid(tampered));
    }

    @Test
    void shouldRejectExpiredToken() {
        JwtService shortLived = new JwtService(secret, -1);
        String token = shortLived.generateAccessToken(user);

        assertFalse(jwtService.isValid(token));
    }

    @Test
    void shouldRejectTokenSignedWithAnotherKey() {
        String otherSecret = Encoders.BASE64.encode(Jwts.SIG.HS256.key().build().getEncoded());
        String token = new JwtService(otherSecret, 15).generateAccessToken(user);

        assertFalse(jwtService.isValid(token));
    }
}