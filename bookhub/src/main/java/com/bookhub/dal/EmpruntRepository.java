package com.bookhub.dal;

import com.bookhub.bo.Commentaire;
import com.bookhub.bo.Emprunt;
import com.bookhub.bo.Livre;
import com.bookhub.bo.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EmpruntRepository extends JpaRepository<Emprunt, Integer> {

    List<Emprunt> findByUtilisateur(Utilisateur utilisateur);

//    pour trouver les retards d'un utilisateur
    List<Emprunt> findByUtilisateurAndDateDeRetourEffectiveIsNullAndDateDeRetourAttendueBefore(
            Utilisateur utilisateur, LocalDateTime now);

//    pour le dashboard des retards du bilbiothécaire
    List<Emprunt> findByDateDeRetourEffectiveIsNullAndDateDeRetourAttendueBefore(LocalDateTime now);

}
