package com.bookhub.bll;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bookhub.bo.Utilisateur;
import com.bookhub.dal.UtilisateurRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public void ajouterUtilisateur(Utilisateur utilisateur) {
        utilisateur.setMotDePasse(
            passwordEncoder.encode(utilisateur.getMotDePasse())
        );
        utilisateurRepository.save(utilisateur);
    }

    @Override
    public Utilisateur modifierUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public void supprimerUtilisateur(String email) {
        utilisateurRepository.deleteById(email);
    }
}