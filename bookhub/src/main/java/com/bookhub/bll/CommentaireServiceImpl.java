package com.bookhub.bll;

import com.bookhub.bo.Commentaire;
import com.bookhub.dal.CommentaireRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
@AllArgsConstructor
public class CommentaireServiceImpl implements CommentaireService {

    public static final int NOTE_MIN = 0;
    public static final int NOTE_MAX = 5;

    private CommentaireRepository commentaireRepository;
    private MessageSource messageSource;

    private void validerCommentaire(Commentaire commentaire) {
        if (commentaire == null) throw new RuntimeException(messageSource.getMessage("review.required", null, Locale.getDefault()));
        if (commentaire.getLivre() == null) throw new RuntimeException(messageSource.getMessage("book.required", null, Locale.getDefault()));
        if (commentaire.getUtilisateur() == null) throw new RuntimeException(messageSource.getMessage("bookhub_user.required", null, Locale.getDefault()));
        if (commentaire.getNote() == null) throw new RuntimeException(messageSource.getMessage("validation.required", null, Locale.getDefault()));
        if (commentaire.getNote() < NOTE_MIN || commentaire.getNote() > NOTE_MAX)
            throw new RuntimeException(messageSource.getMessage("review.correct.note", null, Locale.getDefault()));
        commentaire.setDateDeCreation(LocalDateTime.now());

    }

    /**
     * Enregistre un avis utilisateur sur un livre.
     * Vérifie que la note est comprise entre 0 et 5.
     * @param commentaire L'avis à sauvegarder.
     * @return Le commentaire enregistré avec sa date de création.
     */
    @Override
    public Commentaire laisserCommentaire(Commentaire commentaire) {
        validerCommentaire(commentaire);
        try {
            return commentaireRepository.save(commentaire);
        } catch (RuntimeException e) {
            throw new RuntimeException("Impossible de sauver - " + commentaire.toString());
        }
    }

    /**
     * Modifie un commentaire existant en base de données.
     * Met à jour uniquement le texte de l'avis et la note associée.
     * @param commentaireEnvoye Objet contenant l'ID du commentaire et les nouvelles données.
     * @return Le commentaire mis à jour.
     * @throws RuntimeException si le commentaire n'existe pas en base.
     */
    @Transactional
    @Override
    public Commentaire modifierCommentaire(Commentaire commentaireEnvoye) {
        Commentaire comEnBase = commentaireRepository.findById(commentaireEnvoye.getId())
                .orElseThrow(() -> new RuntimeException("Commentaire introuvable"));
        comEnBase.setCommentaire(commentaireEnvoye.getCommentaire());
        comEnBase.setNote(commentaireEnvoye.getNote());
        return commentaireRepository.save(comEnBase);
    }

    /**
     * Supprime un commentaire de la base de données.
     * @param id Identifiant unique du commentaire à supprimer.
     */
    @Override
    public void supprimerCommentaire(Integer id) {
        commentaireRepository.deleteById(id);
    }
}
