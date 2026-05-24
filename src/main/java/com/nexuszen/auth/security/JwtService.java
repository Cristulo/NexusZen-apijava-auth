package com.nexuszen.auth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

  @Value("${jwt.secret:defaultSecretKeyForDevelopmentMustBeChanged1234567890}")
  private String jwtSecret;

  @Value("${jwt.expiration:86400000}") // 1 día por defecto
  private long jwtExpirationMs;

  private SecretKey getSigningKey() {
    byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateToken(String email, Map<String, Object> claims) {
    return Jwts.builder()
        .claims(claims)
        .subject(email)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
        .signWith(getSigningKey())
        .compact();
  }
}
