package com.bookhub.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // API rest (aucune session http, ne garde aucun etat user)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                // endpoints publics (via auth)
                .requestMatchers("/api/auth/**").permitAll()

                // le reste -> protégé
                .anyRequest().authenticated()
            )

            // pas de comportement dans le navigateur
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());
            
        // TODO config JWT (filtre, valider token et l'injecter, gérer rôles...)

        return http.build();
    }
}
