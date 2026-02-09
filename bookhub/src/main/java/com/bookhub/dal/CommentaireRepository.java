package com.bookhub.dal;

import com.bookhub.bo.Commentaire;
import com.bookhub.bo.Livre;
import com.bookhub.bo.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentaireRepository extends JpaRepository<Commentaire, Integer> {

    List<Commentaire> findByUtilisateur(Utilisateur utilisateur);
    List<Commentaire> findByLivre(Livre livre);

}
