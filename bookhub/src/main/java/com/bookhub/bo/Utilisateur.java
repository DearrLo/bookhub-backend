package com.bookhub.bo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

	@Column(name = "NAME", nullable = false, length = 100)
	private String prenom;

	@Column(name = "SURNAME", nullable = false, length = 100)
	private String nom;

	@Column(name = "PSEUDONYM", length = 100)
	private String pseudo;

	@Column(name = "PASSWORD", nullable = false, length = 255)
	private String motDePasse;

	@Column(name = "ROLE", nullable = false, length = 20)
	private String role;
}
