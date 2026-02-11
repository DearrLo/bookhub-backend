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

    /**
     * Valide la conformité des données d'un utilisateur avant traitement.
     * Vérifie la présence de l'email, du nom, du rôle et du mot de passe.
     * Contrôle également que l'email n'est pas déjà utilisé en base de données.
     * @param utilisateur L'entité utilisateur à vérifier.
     * @throws RuntimeException si une information obligatoire manque ou si l'email existe déjà.
     */
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

    /**
     * Enregistre un nouvel utilisateur en base de données.
     * Procède à la validation des données puis à l'encodage du mot de passe via BCrypt.
     * @param utilisateur Les informations du nouvel utilisateur.
     * @throws RuntimeException en cas d'échec de la sauvegarde.
     */
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

    /**
     * Met à jour les informations de profil d'un utilisateur existant.
     * Permet de modifier le nom, le prénom, le pseudo et optionnellement le mot de passe.
     * @param utilisateurModifie Objet contenant les nouvelles données.
     * @return L'utilisateur mis à jour et sauvegardé.
     * @throws RuntimeException si l'utilisateur n'est pas trouvé par son email.
     */
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

    /**
     * Supprime définitivement un utilisateur de la base de données.
     * @param email L'identifiant (email) de l'utilisateur à supprimer.
     */
    @Override
    public void supprimerUtilisateur(String email) {
        utilisateurRepository.deleteById(email);
    }
}