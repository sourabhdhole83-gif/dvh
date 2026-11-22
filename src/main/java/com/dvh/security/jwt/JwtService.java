package com.dvh.security.jwt;

import com.dvh.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key; // HMAC SecretKey used to sign and verify JWT signatures.
    private final long accessTokenExpiryMs; // Access token lifetime converted from minutes to milliseconds.

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.access-token-expiry-minutes}") long expiryMinutes) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); // Decodes Base64 secret into bytes and creates an HMAC SecretKey.
        this.accessTokenExpiryMs = expiryMinutes * 60 * 1000; // Converts configured minutes into milliseconds.
    }

    public String generateAccessToken(User user) {
        Date now = new Date(); // Captures the current timestamp for token issuance and expiry calculation.
        return Jwts.builder()
                .subject(user.getId().toString()) // Sets the standard 'sub' payload claim to the user's ID.
                .claim("email", user.getEmail()) // Adds a custom 'email' claim into the JWT payload.
                .claim("role", user.getRole().name()) // Adds a custom 'role' claim into the JWT payload.
                .issuedAt(now) // Sets the standard 'iat' payload claim for when the token was created.
                .expiration(new Date(now.getTime() + accessTokenExpiryMs)) // Sets the standard 'exp' payload claim for when the token expires.
                .signWith(key) // Computes the HMAC signature using the SecretKey to prevent tampering.
                .compact(); // Serializes header, payload, and signature into a compact JWT string.
    }

    public Claims parseToken(String token) {
        return Jwts.parser().verifyWith(key).build() // Configures the JWT parser with the SecretKey for signature verification.
                .parseSignedClaims(token).getPayload(); // Verifies signature and expiration, then returns the payload claims.
    }

    public boolean isValid(String token) {
        try {
            parseToken(token); // Attempts to parse and validate the token's signature and expiration.
            return true; // Returns true if parsing succeeds without errors.
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Returns false if the token is invalid, expired, or malformed.
        }
    }
}