package com.bookhub.dal;

import com.bookhub.bo.Emprunt;
import com.bookhub.bo.StatutEmprunt;
import com.bookhub.bo.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface de persistance pour la gestion des emprunts.
 * Permet le suivi des flux de prêts, des retours et l'identification des retards.
 */
public interface EmpruntRepository extends JpaRepository<Emprunt, Integer> {

    /** Récupère l'historique complet des emprunts d'un utilisateur trié par date. */
    List<Emprunt> findByUtilisateur_EmailOrderByDateDEmpruntDesc(String email);

    /** Récupère tous les emprunts ayant un statut spécifique (ex: non rendus). */
    List<Emprunt> findByStatutNot(StatutEmprunt statut);

    /** Compte le nombre d'emprunts actifs pour un utilisateur (livres non encore restitués). */
    long countByUtilisateur_EmailAndDateDeRetourEffectiveIsNull(String email);

    /** Identifie les emprunts en retard pour un utilisateur spécifique. */
    List<Emprunt> findByUtilisateurAndDateDeRetourEffectiveIsNullAndDateDeRetourAttendueBefore(
            Utilisateur utilisateur, LocalDateTime now);

    /** Identifie tous les emprunts en retard dans la bibliothèque (pour le dashboard bibliothécaire). */
    List<Emprunt> findByDateDeRetourEffectiveIsNullAndDateDeRetourAttendueBefore(LocalDateTime now);
}