package com.arrowacademy.user_service.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Map;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    public JSONObject parseTokenAsJSON(String token) {
        try {
            String base64Key = "KNXjf1tRI4wqjO/Zbd1dshwcihfmaChcgW6Yft+wid0=";
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
