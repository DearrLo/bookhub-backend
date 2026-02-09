package com.bookhub.dal;

import com.bookhub.bo.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {
	boolean existsByEmail(String email);

	Optional<Utilisateur> findByEmail(String email);
}