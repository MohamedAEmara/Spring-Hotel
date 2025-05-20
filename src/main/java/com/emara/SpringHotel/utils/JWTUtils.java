package com.emara.SpringHotel.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTUtils {
   private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 7 days

   @Value("${jwt.secret}")
   private String secret;

   private SecretKey key;

   @PostConstruct
   public void init() {
       if (secret == null || secret.isEmpty()) {
           throw new IllegalStateException("JWT secret is not configured");
       }
       System.out.println("Secretttttttttttttttttttttttttttttt: " + secret);
       byte[] keyBytes = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
       this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
   }

    public String generateToken(UserDetails userDetails) {

        System.out.println(userDetails);
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256) // ✅ explicitly set algorithm
                .compact();
    }

   public String extractUsername(String token) {
       return extractClaims(token, Claims::getSubject);
   }

   public boolean isValidToken(String token, UserDetails userDetails) {
       final String username = extractUsername(token);
       return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
   }

   private boolean isTokenExpired(String token) {
       return extractClaims(token, Claims::getExpiration).before(new Date());
   }

   private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
       return claimsResolver.apply(
               Jwts.parser()
                       .verifyWith(key)
                       .build()
                       .parseSignedClaims(token)
                       .getPayload()
       );
   }
}