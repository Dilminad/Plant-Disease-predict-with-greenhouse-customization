package com.example.server.Security;

import com.example.server.Security.Jwt.AuthEntryPoint;
import com.example.server.Security.Jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Main security configuration class for the application.
 * Enables web security, method-level security, and configures JWT, CORS, and protected endpoints.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthEntryPoint unauthorizedHandler;

    /**
     * Creates a bean for the custom JWT authentication filter.
     * This filter is responsible for validating the JWT token from the request header.
     * @return An instance of AuthTokenFilter.
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    /**
     * Defines the password encoder for the application (BCrypt).
     * @return A PasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the primary authentication provider (DAO).
     * It connects Spring Security to the CustomUserDetailsService and sets the password encoder.
     * @return A DaoAuthenticationProvider instance.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Exposes the AuthenticationManager as a bean from the AuthenticationConfiguration.
     * Required for the authentication process.
     * @param authConfig The authentication configuration.
     * @return An AuthenticationManager instance.
     * @throws Exception if an error occurs while getting the manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Defines the main security filter chain that configures HTTP security settings.
     * @param http The HttpSecurity object to configure.
     * @return A SecurityFilterChain instance.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Apply CORS configuration from the corsConfigurationSource bean
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // Disable CSRF protection as we are using stateless JWT authentication
            .csrf(csrf -> csrf.disable())
            // Set the custom authentication entry point for handling 401 Unauthorized errors
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            // Configure session management to be stateless; no sessions are created on the server
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Define authorization rules for different endpoints
            .authorizeHttpRequests(auth -> auth
                // Permit all requests to authentication-related endpoints
                .requestMatchers("/auth/**").permitAll()
                // Permit all requests to WebSocket endpoints
                .requestMatchers("/ws/**").permitAll()
                // Require authentication for any other request
                .requestMatchers("/api/classify").permitAll()
                .anyRequest().authenticated()
            );

        // Set the custom authentication provider
        http.authenticationProvider(authenticationProvider());

        // Add the custom JWT filter before the standard UsernamePasswordAuthenticationFilter
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Defines the global CORS configuration for the application.
     * This allows the frontend application (e.g., running on localhost:5173) to make requests.
     * @return A CorsConfigurationSource instance.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Define the allowed origin for your React frontend
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        // Define the allowed HTTP methods
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Allow all headers
        configuration.setAllowedHeaders(List.of("*"));
        // Allow credentials (e.g., cookies, authorization headers)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply this CORS configuration to all paths in the application
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
