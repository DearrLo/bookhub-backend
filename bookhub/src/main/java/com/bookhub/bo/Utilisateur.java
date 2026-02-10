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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "email" })
@Entity

@Table(name = "BOOKHUB_USER")
public class Utilisateur {

	@Id
	@Column(name = "EMAIL", length = 255)
	private String email;

	@NotNull
	@Column(name = "NAME", nullable = false, length = 100)
	private String prenom;

	@NotNull
	@Column(name = "SURNAME", nullable = false, length = 100)
	private String nom;

	@NotNull
	@Column(name = "PSEUDONYM", length = 100)
	private String pseudo;

	@NotBlank
	@Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).*$",
			message = "Le mot de passe doit contenir une majuscule, une minuscule et un chiffre")
	@Column(name = "PASSWORD", nullable = false, length = 255)
	private String motDePasse;

	@NotNull
	@Column(name = "ROLE", nullable = false, length = 20)
	private String role;
}
