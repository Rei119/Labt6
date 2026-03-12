package com.soap;

import jakarta.jws.WebService;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@WebService(endpointInterface = "com.soap.AuthService")
public class AuthServiceImpl implements AuthService {

    private static Map<String, String> users = new HashMap<>();
    private static Map<String, String> userRoles = new HashMap<>();

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Override
    public String registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return "ERROR: User already exists";
        }
        users.put(username, password);
        userRoles.put(username, "USER");
        return "SUCCESS: User registered";
    }

    @Override
    public String registerAdmin(String username, String password) {
        if (users.containsKey(username)) {
            return "ERROR: User already exists";
        }
        users.put(username, password);
        userRoles.put(username, "ADMIN");
        return "SUCCESS: Admin registered";
    }

    @Override
    public String loginUser(String username, String password) {
        if (users.containsKey(username) && users.get(username).equals(password)) {
            String token = Jwts.builder()
                .setSubject(username)
                .claim("role", userRoles.get(username))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SECRET_KEY)
                .compact();
            return token;
        }
        return "ERROR: Invalid credentials";
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getUserRole(String token) {
        return getRoleFromToken(token);
    }

    public String getRoleFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
            return claims.get("role", String.class);
        } catch (Exception e) {
            return "NONE";
        }
    }
}