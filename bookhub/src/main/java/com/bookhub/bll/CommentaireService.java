package com.bookhub.bll;

import com.bookhub.bo.Commentaire;

public interface CommentaireService {
    Commentaire laisserCommentaire(Commentaire commentaire);

    Commentaire modifierCommentaire(Commentaire commentaire);

    void supprimerCommentaire(Integer id);
}
