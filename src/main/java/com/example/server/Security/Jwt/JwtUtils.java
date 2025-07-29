package com.example.server.Security.Jwt;

import com.example.server.repository.RolesRepo.FarmerRepository;
import com.example.server.repository.RolesRepo.BuyerRepository;
import com.example.server.repository.RolesRepo.AdminRepository;
import com.example.server.repository.RolesRepo.SeedSellerRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.secret:default-secret-key}")
    private String secret;

    @Value("${app.jwt.expiration:86400000}")
    private long expirationTime;

    @Autowired
    private FarmerRepository farmerRepository;
    
    @Autowired
    private BuyerRepository buyerRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private SeedSellerRepository seedSellerRepository;

    private Key key() {
        try {
            byte[] decodedKey = Decoders.BASE64.decode(secret);
            logger.info("Secret: {}, Decoded key length: {} bytes", secret, decodedKey.length);
            if (decodedKey.length < 32) {
                throw new IllegalArgumentException("Secret key is only " + (decodedKey.length * 8) + " bits. Must be at least 256 bits.");
            }
            return Keys.hmacShaKeyFor(decodedKey);
        } catch (Exception e) {
            logger.error("Failed to decode secret key: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid secret key", e);
        }
    }

    public String generateJwtToken(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalArgumentException("Authentication or principal cannot be null");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = getRoleFromUserDetails(userDetails);
        String userId = getUserIdFromDatabase(userDetails, role);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("role", role)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expirationTime))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String getRoleFromUserDetails(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new IllegalStateException("User has no roles assigned"));
    }

    private String getUserIdFromDatabase(UserDetails userDetails, String role) {
        String username = userDetails.getUsername();
        String cleanRole = role.replace("ROLE_", "");

        try {
            return switch (cleanRole) {
                case "FARMER" -> farmerRepository.findByUsername(username)
                        .map(f -> f.getId().toString())
                        .orElseThrow(() -> new RuntimeException("Farmer not found"));
                case "BUYER" -> buyerRepository.findByUsername(username)
                        .map(b -> b.getId().toString())
                        .orElseThrow(() -> new RuntimeException("Buyer not found"));
                case "ADMIN" -> adminRepository.findByUsername(username)
                        .map(a -> a.getId().toString())
                        .orElseThrow(() -> new RuntimeException("Admin not found"));
                case "SEED_SELLER" -> seedSellerRepository.findByUsername(username)
                        .map(s -> s.getId().toString())
                        .orElseThrow(() -> new RuntimeException("Seed seller not found"));
                default -> throw new IllegalArgumentException("Unknown role: " + cleanRole);
            };
        } catch (Exception e) {
            logger.error("Failed to get user ID for {}: {}", username, e.getMessage());
            throw new RuntimeException("Failed to retrieve user ID", e);
        }
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            return true;
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.getMessage(), e);
            return false;
        }
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String getUserIdFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().get("userId", String.class);
    }
}