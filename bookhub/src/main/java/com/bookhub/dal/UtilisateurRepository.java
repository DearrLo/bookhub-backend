package com.bookhub.dal;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookhub.bo.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {
	boolean existsByEmail(String email);

	Optional<Utilisateur> findByEmail(String email);
}