package com.example.server.controller.RoleCntroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.server.Security.Jwt.JwtUtils;
import com.example.server.dto.LoginResponse;
import com.example.server.dto.Logindto;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody Logindto loginDto) {
        System.out.println("Login attempt for username: " + loginDto.getUserName() + " with password: " + loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("Authentication successful for: " + loginDto.getUserName());

        String jwtToken = jwtUtils.generateJwtToken(authentication);
        System.out.println("Generated JWT: " + jwtToken);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(auth -> auth.replace("ROLE_", ""))
                .orElse("USER");
        System.out.println("User role: " + role);

        String userId = jwtUtils.getUserIdFromJwtToken(jwtToken);
        System.out.println("User ID: " + userId);

        return new LoginResponse(jwtToken, role, userId);
    }
}