package com.arrow_academy.auth_service.services;

import com.arrow_academy.auth_service.dao.UserRepo;
import com.arrow_academy.auth_service.model.User;
import com.arrow_academy.auth_service.model.UserWrapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.security.Key;
import java.util.function.Function;

@Service
public class JwtService {
    @Autowired
    private UserRepo repo;

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(UserWrapper user) {
        Map<String, Object> claims = new HashMap<>();
        User currUser = repo.findByUsername(user.getUsername());
        String role = currUser.getRole();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 120)) // 120 min
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public JSONObject parseTokenAsJSON(String token) {
        try {
            // Decode the Base64 key
            byte[] decodedKey = Base64.getDecoder().decode(secretKey);
            SecretKey key = Keys.hmacShaKeyFor(decodedKey);

            // Parse the JWT
            Jws<Claims> jwsClaims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            Claims claims = jwsClaims.getBody();

            // Convert claims map to JSONObject
            JSONObject json = new JSONObject();
            for (Map.Entry<String, Object> entry : claims.entrySet()) {
                json.put(entry.getKey(), entry.getValue());
            }

            return json;
        } catch (JwtException e) {
            System.out.println("Invalid token: " + e.getMessage());
            return null;
        }
    }
}
