package com.bookhub.bll;

import com.bookhub.bo.Commentaire;

public interface CommentaireService {
    Commentaire laisserCommentaire(Commentaire commentaire);

    void supprimerCommentaire(Integer id);
}
