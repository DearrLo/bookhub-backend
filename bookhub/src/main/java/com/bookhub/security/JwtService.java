package com.bookhub.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtService {

	@Value("${security.jwt.secret}")
	private String secret;

	// durée de vie du token
	@Value("${security.jwt.expiration-ms:86400000}")
	private long expirationMs;

	// génère JWT
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> extraClaims = new HashMap<>();

		extraClaims.put("roles", userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList()));

		Date now = new Date();
		Date expiry = new Date(now.getTime() + expirationMs);

		return Jwts.builder()
				.setClaims(extraClaims) // On injecte les rôles ici
				.setSubject(userDetails.getUsername())
				.setIssuedAt(now)
				.setExpiration(expiry)
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	// récupère l’email depuis le token
	public String extractUsername(String token) {
		return extractAllClaims(token).getSubject();
	}

	// vérifie que le token match l'utilisateur
	public boolean isTokenValid(String token, UserDetails userDetails) {
		String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		return extractAllClaims(token).getExpiration().before(new Date());
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}

	private Key getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
