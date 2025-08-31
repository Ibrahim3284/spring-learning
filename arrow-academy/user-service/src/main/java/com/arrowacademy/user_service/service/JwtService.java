package com.arrowacademy.user_service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Map;

@Service
public class JwtService {

    // Inject the secret key from application.properties
    @Value("${spring.security.oauth2.resourceserver.jwt.secret-key}")
    private String base64Key;

    public JSONObject parseTokenAsJSON(String token) {
        try {
            // Decode the Base64 key
            byte[] decodedKey = Base64.getDecoder().decode(base64Key);
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
