package com.bookhub.bll;

import com.bookhub.bo.Utilisateur;
import com.bookhub.dal.UtilisateurRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Service gérant la logique métier des utilisateurs.
 * Inclut la validation, l'inscription, la modification et la suppression.
 */
@AllArgsConstructor
@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final MessageSource messageSource;
    private final PasswordEncoder passwordEncoder;

    /**
     * Valide la conformité des données d'un utilisateur avant traitement.
     */
    public void validerUtilisateur(Utilisateur utilisateur){
        if (utilisateur == null) {
            throw new RuntimeException(messageSource.getMessage("user.required", null, Locale.getDefault()));
        }
        if (utilisateur.getEmail() == null) {
            throw new RuntimeException(messageSource.getMessage("user.email.required", null, Locale.getDefault()));
        }
        if (utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
            throw new RuntimeException(messageSource.getMessage("user.email.exists",
                    new Object[]{utilisateur.getEmail()}, Locale.getDefault()));
        }
        if (utilisateur.getPrenom() == null) {
            throw new RuntimeException(messageSource.getMessage("user.surname.required", null, Locale.getDefault()));
        }
        if (utilisateur.getNom() == null) {
            throw new RuntimeException(messageSource.getMessage("user.name.required", null, Locale.getDefault()));
        }
        if (utilisateur.getRole() == null) {
            throw new RuntimeException(messageSource.getMessage("user.role.required", null, Locale.getDefault()));
        }
        if (utilisateur.getMotDePasse() == null) {
            throw new RuntimeException(messageSource.getMessage("user.password.required", null, Locale.getDefault()));
        }
    }

    /**
     * Enregistre un nouvel utilisateur avec encodage du mot de passe.
     */
    @Override
    public void ajouterUtilisateur(Utilisateur utilisateur) {
        validerUtilisateur(utilisateur);
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        try {
            utilisateurRepository.save(utilisateur);
        } catch (RuntimeException e) {
            throw new RuntimeException(messageSource.getMessage("bookhub_user.save.failed", null, Locale.getDefault()));
        }
    }

    /**
     * Met à jour le profil d'un utilisateur existant.
     */
    @Transactional
    @Override
    public Utilisateur modifierUtilisateur(Utilisateur utilisateurModifie) {
        Utilisateur utilisateurEnBase = utilisateurRepository.findById(utilisateurModifie.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        utilisateurEnBase.setNom(utilisateurModifie.getNom());
        utilisateurEnBase.setPrenom(utilisateurModifie.getPrenom());
        utilisateurEnBase.setPseudo(utilisateurModifie.getPseudo());

        // tel : ne pas écraser existant si front n'envoie rien
        if (utilisateurModifie.getTelephone() != null) {
            utilisateurEnBase.setTelephone(utilisateurModifie.getTelephone());
        }

        // mdp : seulement si fourni
        if (utilisateurModifie.getMotDePasse() != null && !utilisateurModifie.getMotDePasse().isEmpty()) {
            utilisateurEnBase.setMotDePasse(passwordEncoder.encode(utilisateurModifie.getMotDePasse()));
        }

        return utilisateurRepository.save(utilisateurEnBase);
    }

    @Override
    public void supprimerUtilisateur(String email) {
        utilisateurRepository.deleteById(email);
    }

    @Override
    public Utilisateur trouverParEmail(String email) {
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}
