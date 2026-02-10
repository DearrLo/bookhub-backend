package com.bookhub.bll;

import com.bookhub.bo.Utilisateur;
import com.bookhub.dal.UtilisateurRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@AllArgsConstructor
@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final MessageSource messageSource;
    private PasswordEncoder passwordEncoder;

    public void validerUtilisateur(Utilisateur utilisateur){
        if (utilisateur == null) {
            throw new RuntimeException(messageSource.getMessage("user.required", null, Locale.getDefault()));
        }
        if (utilisateur.getEmail() == null) {
            throw new RuntimeException(messageSource.getMessage("user.email.required", null, Locale.getDefault()));
        }
        if (utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
            throw new RuntimeException(messageSource.getMessage("user.email.exists", new Object[]{utilisateur.getEmail()}, Locale.getDefault()));
        }
        if (utilisateur.getNom() == null) {
            throw new RuntimeException(messageSource.getMessage("user.name.required", null, Locale.getDefault()));
        }
        if (utilisateur.getRole() == null) {
            throw new RuntimeException(messageSource.getMessage("user.role.required", null, Locale.getDefault()));
        }
        if (utilisateur.getPrenom() == null) {
            throw new RuntimeException(messageSource.getMessage("user.surname.required", null, Locale.getDefault()));
        }
        if (utilisateur.getMotDePasse() == null) {
            throw new RuntimeException(messageSource.getMessage("user.password.required", null, Locale.getDefault()));
        }
    }
    @Override
    public void ajouterUtilisateur(Utilisateur utilisateur) {
        validerUtilisateur(utilisateur);
        utilisateur.setMotDePasse(
                passwordEncoder.encode(utilisateur.getMotDePasse()));
        try {
            utilisateurRepository.save(utilisateur);
        } catch (RuntimeException e) {
            throw new RuntimeException(messageSource.getMessage("bookhub_user.save.failed", null, Locale.getDefault()));
        }
    }

    @Transactional
    public Utilisateur modifierUtilisateur(Utilisateur utilisateurModifie) {
        Utilisateur utilisateurEnBase = utilisateurRepository.findById(utilisateurModifie.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        utilisateurEnBase.setNom(utilisateurModifie.getNom());
        utilisateurEnBase.setPrenom(utilisateurModifie.getPrenom());
        utilisateurEnBase.setPseudo(utilisateurModifie.getPseudo());

        if (utilisateurModifie.getMotDePasse() != null && !utilisateurModifie.getMotDePasse().isEmpty()) {
            utilisateurEnBase.setMotDePasse(passwordEncoder.encode(utilisateurModifie.getMotDePasse()));
        }

        return utilisateurRepository.save(utilisateurEnBase);
    }
    @Override
    public void supprimerUtilisateur(String email) {
        utilisateurRepository.deleteById(email);
    }
}