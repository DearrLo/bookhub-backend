package com.bookhub.dal;

import com.bookhub.bo.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EmpruntRepository extends JpaRepository<Emprunt, Integer> {

    List<Emprunt> findByUtilisateur_EmailOrderByDateDEmpruntDesc(String email);

//    pour le dashboard du bilbiothécaire, tous emprunts en cours (sauf statut rendu)
    List<Emprunt> findByStatutNot(StatutEmprunt statut);

    //  pour compter les emprunts en cours pour UN utilisateur spécifique
    long countByUtilisateur_EmailAndDateDeRetourEffectiveIsNull(String email);

//    pour trouver les retards d'un utilisateur
    List<Emprunt> findByUtilisateurAndDateDeRetourEffectiveIsNullAndDateDeRetourAttendueBefore(
            Utilisateur utilisateur, LocalDateTime now);

//    pour le dashboard du bilbiothécaire, tous les retards
    List<Emprunt> findByDateDeRetourEffectiveIsNullAndDateDeRetourAttendueBefore(LocalDateTime now);

}
