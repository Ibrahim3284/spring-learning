package com.arrow_academy.auth_service.services;

import com.arrow_academy.auth_service.dao.UserRepo;
import com.arrow_academy.auth_service.model.User;
import com.arrow_academy.auth_service.model.UserWrapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import java.security.Key;
import java.util.function.Function;

@Service
public class JwtService {

//    private static final String SECRET = "xyz";

    @Autowired
    private UserRepo repo;

    private String secretKey = "123a";

    public JwtService() {
        secretKey = generateSecretKey();
    }

    public String generateSecretKey() {
//        try{
//            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey secretKey = keyGenerator.generateKey();
//            System.out.println("Secret key: " + secretKey.toString());
//            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("Error generating secret key", e);
//        }
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // 256-bit key
        String base64EncodedKey = java.util.Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println(base64EncodedKey);
        return base64EncodedKey;
    }

    public String generateToken(UserWrapper user){

        Map<String, Object> claims = new HashMap<>();
        User currUser = repo.findByUsername(user.getUsername());
        String role = currUser.getRole();
        claims.put("role", role);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*3))
                .signWith(getKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
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
}
