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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // Publiques
                        .requestMatchers("/api/auth/**").permitAll()

                        // Livres
                        .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/books").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.PUT, "/api/books/**").hasRole("LIBRARIAN")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")

                        // Emprunts
                        .requestMatchers(HttpMethod.GET, "/api/loans").hasRole("LIBRARIAN")

                        /// api/loans/my = mes emprunts / get (user)
                        .requestMatchers(HttpMethod.GET, "/api/loans/my").hasRole("USER")

                        /// api/loans = emprunter / post (user)
                        .requestMatchers(HttpMethod.POST, "/api/loans").hasRole("USER")

                        /// api/loans/{id}/return = valider retour / put (bibliothécaire)
                        .requestMatchers(HttpMethod.PUT, "/api/loans/*/return").hasRole("LIBRARIAN")

                        // Réservations
                        .requestMatchers(HttpMethod.GET, "/api/reservations/my").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/reservations").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/reservations/**").hasRole("USER")

                        // Commentaires
                        .requestMatchers(HttpMethod.POST, "/api/books/*/ratings").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/api/ratings/*").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/ratings/**").hasRole("LIBRARIAN")

                        .anyRequest().authenticated())

                .formLogin(form -> form.disable()).httpBasic(basic -> basic.disable())

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
