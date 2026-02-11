package com.bookhub.dal;

import com.bookhub.bo.Commentaire;
import com.bookhub.bo.Livre;
import com.bookhub.bo.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Interface de persistance pour les avis et notes des lecteurs.
 */
public interface CommentaireRepository extends JpaRepository<Commentaire, Integer> {

    /** Récupère tous les commentaires rédigés par un utilisateur. */
    List<Commentaire> findByUtilisateur(Utilisateur utilisateur);

    /** Récupère tous les commentaires associés à un livre spécifique. */
    List<Commentaire> findByLivre(Livre livre);
}