package com.bookhub.dal;

import com.bookhub.bo.Reservation;
import com.bookhub.bo.StatutResa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * Interface de persistance pour les réservations.
 * Gère les demandes d'ouvrages hors stock et la file d'attente des lecteurs.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    /** Récupère une réservation spécifique pour un couple livre/utilisateur. */
    Optional<Reservation> findByLivre_IdAndUtilisateur_Email(Integer idLivre, String emailUtilisateur);

    /** Liste les réservations d'un lecteur par date de demande décroissante. */
    List<Reservation> findByUtilisateur_EmailOrderByDateDeDemandeDesc(String email);

    /** Compte les réservations actives d'un utilisateur pour l'application des quotas. */
    long countByUtilisateur_EmailAndStatutNot(String email, StatutResa statut);

    /** Gère la file d'attente chronologique pour un livre spécifique. */
    List<Reservation> findByLivre_IdAndStatutOrderByDateDeDemandeAsc(Integer livreId, StatutResa statut);
}