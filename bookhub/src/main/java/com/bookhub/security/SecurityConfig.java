package com.bookhub.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;

/**
 * Classe de configuration principale de Spring Security.
 * Définit la politique de CORS, de CSRF, la gestion des sessions sans état (Stateless)
 * et les droits d'accès par URL et par rôle.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Configure la chaîne de filtres de sécurité.
     * Définit les accès publics (Swagger, Auth) et les routes protégées par rôles (USER, LIBRARIAN, ADMIN).
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(org.springframework.security.config.Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // Accès publics : Authentification et Documentation API
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Catalogue de livres
                        .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/books").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.PUT, "/api/books/**").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")

                        // Gestion des Emprunts
                        .requestMatchers(HttpMethod.GET, "/api/loans").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.GET, "/api/loans/my").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/loans").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/loans/*/return").hasRole("LIBRARIAN")

                        // Gestion des Réservations
                        .requestMatchers(HttpMethod.GET, "/api/reservations/my").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/reservations").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/reservations/**").hasRole("USER")

                        // Gestion des Commentaires (Avis)
                        .requestMatchers(HttpMethod.POST, "/api/books/*/ratings").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/ratings/*").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/ratings/**").hasRole("LIBRARIAN")

                        .anyRequest().authenticated())

                .formLogin(form -> form.disable()).httpBasic(basic -> basic.disable())

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Définit l'encodeur de mot de passe utilisé pour BCrypt (force 12).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}