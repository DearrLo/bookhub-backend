package com.bookhub.bll;

import com.bookhub.bo.*;
import com.bookhub.dal.CommentaireRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CommentaireServiceImpl implements CommentaireService{

    private CommentaireRepository commentaireRepository;

    private void validerCommentaire(Commentaire commentaire) {
        if (commentaire == null) throw new RuntimeException("Le commentaire est obligatoire");
        if (commentaire.getLivre() == null) throw new RuntimeException("Le livre est obligatoire");
        if (commentaire.getUtilisateur() == null) throw new RuntimeException("Le lecteur est obligatoire");
        if (commentaire.getNote() == null) throw new RuntimeException("La note est obligatoire");
        if (commentaire.getNote() < 0 || commentaire.getNote() > 5 ) throw new RuntimeException("La note doit être comprise entre 0 et 5");
        commentaire.setDateDeCreation(LocalDateTime.now());

    }

    @Override
    public Commentaire laisserCommentaire(Commentaire commentaire) {
        validerCommentaire(commentaire);
        try {
            return commentaireRepository.save(commentaire);
        } catch (RuntimeException e) {
            throw new RuntimeException("Impossible de sauver - " + commentaire.toString());
        }
    }

    @Override
    public void supprimerCommentaire(Integer id) {
        commentaireRepository.deleteById(id);
    }
}
