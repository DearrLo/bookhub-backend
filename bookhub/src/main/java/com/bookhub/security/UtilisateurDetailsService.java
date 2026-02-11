package com.bookhub.security;

import com.bookhub.bo.Utilisateur;
import com.bookhub.dal.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service permettant de charger les données d'un utilisateur à partir de la base de données
 * pour les besoins de l'authentification Spring Security.
 */
@Service
@RequiredArgsConstructor
public class UtilisateurDetailsService implements UserDetailsService {

	private final UtilisateurRepository utilisateurRepository;

	/**
	 * Recherche un utilisateur par son email et convertit l'entité Utilisateur
	 * en objet UserDetails compréhensible par Spring Security.
	 * Ajoute automatiquement le préfixe 'ROLE_' au rôle stocké en base.
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Utilisateur utilisateur = utilisateurRepository.findById(email)
				.orElseThrow(() -> new UsernameNotFoundException(
						"Utilisateur introuvable"));

		return new org.springframework.security.core.userdetails.User(
				utilisateur.getEmail(),
				utilisateur.getMotDePasse(),
				List.of(new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole()))
		);
	}
}