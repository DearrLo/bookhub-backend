package com.bookhub.security;

import com.bookhub.dal.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service permettant de charger les données d'un utilisateur à partir de la base de données
 * pour les besoins de l'authentification Spring Security.
 */
@Service
@RequiredArgsConstructor
public class UtilisateurDetailsService implements UserDetailsService {

	private final UtilisateurRepository utilisateurRepository;


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// On retourne directement l'utilisateur trouvé car il est un UserDetails
		return utilisateurRepository.findById(email)
				.orElseThrow(() -> new UsernameNotFoundException(
						"Utilisateur introuvable avec l'email : " + email));
	}
}