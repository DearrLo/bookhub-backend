package com.bookhub.bo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Représente un utilisateur de la plateforme BookHub.
 * Gère les informations de profil, les identifiants et les rôles de sécurité.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "email" })
@Entity
@Table(name = "BOOKHUB_USER")
public class Utilisateur implements UserDetails {

	/** Email de l'utilisateur servant d'identifiant unique. */
	@Id
	@Column(name = "EMAIL", length = 255)
	private String email;

	/** Prénom de l'utilisateur. */
	@NotNull
	@Column(name = "NAME", nullable = false, length = 100)
	private String prenom;

	/** Nom de famille de l'utilisateur. */
	@NotNull
	@Column(name = "SURNAME", nullable = false, length = 100)
	private String nom;
	
	@Column(name = "PHONE", length = 20)
	private String telephone;

	/** Pseudo affiché publiquement (ex: pour les commentaires). */
	@NotNull
	@Column(name = "PSEUDONYM", length = 100)
	private String pseudo;

	/** Mot de passe encrypté (BCrypt). Doit respecter les règles de complexité. */
	@NotBlank
	@Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$",
			message = "Le mot de passe doit contenir une majuscule, une minuscule et un chiffre")
	@Column(name = "PASSWORD", nullable = false, length = 255)
	private String motDePasse;

	/** Rôle de l'utilisateur (ex: ROLE_USER, ROLE_LIBRARIAN, ROLE_ADMIN). */
	@NotNull
	@Column(name = "ROLE", length = 50)
	private String role;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}

	@Override
	public @Nullable String getPassword() {
		return "";
	}

	@Override
	public String getUsername() {
		return "";
	}

	@Override
	public boolean isAccountNonExpired() {
		return UserDetails.super.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return UserDetails.super.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return UserDetails.super.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		return UserDetails.super.isEnabled();
	}
}