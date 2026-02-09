package com.bookhub.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bookhub.bo.Utilisateur;
import com.bookhub.dal.UtilisateurRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UtilisateurDetailsService implements UserDetailsService {

	private final UtilisateurRepository utilisateurRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Utilisateur utilisateur = utilisateurRepository	.findById(email)
														.orElseThrow(() -> new UsernameNotFoundException(
																"Utilisateur introuvable"));

		return new org.springframework.security.core.userdetails.User(utilisateur.getEmail(),
				utilisateur.getMotDePasse(), List.of(new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole())));
	}
}
