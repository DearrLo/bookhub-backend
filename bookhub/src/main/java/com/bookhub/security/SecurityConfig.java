package com.bookhub.security;

import java.util.List;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.csrf(csrf -> csrf.disable())

			// CORS - autorisation Angular pour appels d'API
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))

			.authorizeHttpRequests(auth -> auth

				// CORS - autorise aussi les requêtes techniques (préflight)
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

				// Publiques
				.requestMatchers("/api/auth/**").permitAll()

				.requestMatchers(
						"/v3/api-docs/**",
						"/swagger-ui/**",
						"/swagger-ui.html"
				).permitAll()

				// Livres
				.requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/books").hasRole("LIBRARIAN")
				.requestMatchers(HttpMethod.PUT, "/api/books/**").hasRole("LIBRARIAN")
				.requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")

				// Emprunts
				.requestMatchers(HttpMethod.GET, "/api/loans").hasRole("LIBRARIAN")

				// /api/loans/my = mes emprunts (USER)
				.requestMatchers(HttpMethod.GET, "/api/loans/my").hasRole("USER")

				// /api/loans = emprunter (USER)
				.requestMatchers(HttpMethod.POST, "/api/loans").hasRole("USER")

				// /api/loans/{id}/return = valider retour (LIBRARIAN)
				.requestMatchers(HttpMethod.PUT, "/api/loans/*/return").hasRole("LIBRARIAN")

				// Réservations
				.requestMatchers(HttpMethod.GET, "/api/reservations/my").hasRole("USER")
				.requestMatchers(HttpMethod.POST, "/api/reservations").hasRole("USER")
				.requestMatchers(HttpMethod.DELETE, "/api/reservations/**").hasRole("USER")

				// Commentaires / Ratings
				.requestMatchers(HttpMethod.POST, "/api/books/*/ratings").hasRole("USER")
				.requestMatchers(HttpMethod.PUT, "/api/ratings/*").hasRole("USER")
				.requestMatchers(HttpMethod.DELETE, "/api/ratings/**").hasRole("LIBRARIAN")

				.anyRequest().authenticated()
			)

			.formLogin(form -> form.disable())
			.httpBasic(basic -> basic.disable())

			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// CORS - règles et autorisations pour appels API (méthodes, headers & identité)
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		// Autorise seulement le front Angular local (port 4200)
		config.setAllowedOrigins(List.of("http://localhost:4200"));

		// Autorise les méthodes permises
		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

		// Autorise les headers JWT et JSON envoyés par le front
		config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

		// Autorise l’envoi de credentials (cookies / auth)
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return source;
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
