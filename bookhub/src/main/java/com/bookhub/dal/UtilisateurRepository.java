package com.bookhub.dal;

import com.bookhub.bo.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Interface de persistance pour les utilisateurs.
 */
public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {

	/** Vérifie si un compte existe déjà avec cet email. */
	boolean existsByEmail(String email);

	/** Recherche un utilisateur par son identifiant unique (email). */
	Optional<Utilisateur> findByEmail(String email);
}