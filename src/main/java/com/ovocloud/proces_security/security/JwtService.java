package com.ovocloud.proces_security.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private static final String SECRET_KEY = "MySecretKeyForProcessSecurityApplication2025VeryLongAndSecure256BitsKey!!!";
    private static final long EXPIRATION_TIME = 3600000;
    private static final long REFRESH_EXPIRATION_TIME = 86400000;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Integer idUser, Integer idBusiness, Integer role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("idUser", idUser);
        claims.put("idBusiness", idBusiness);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(idUser))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Integer idUser) {
        return Jwts.builder()
                .setSubject(String.valueOf(idUser))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Long getExpirationTime() {
        return System.currentTimeMillis() + EXPIRATION_TIME;
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Integer extractIdUser(String token) {
        Claims claims = extractClaims(token);
        return claims.get("idUser", Integer.class);
    }

    public Integer extractIdBusiness(String token) {
        Claims claims = extractClaims(token);
        return claims.get("idBusiness", Integer.class);
    }

    public Integer extractRole(String token) {
        Claims claims = extractClaims(token);
        return claims.get("role", Integer.class);
    }
}
