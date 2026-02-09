package com.bookhub.dal;

import com.bookhub.bo.Reservation;
import com.bookhub.bo.StatutResa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

	Optional<Reservation> findByLivre_IdAndUtilisateur_Email(Integer idLivre, String emailUtilisateur);

    List<Reservation> findByUtilisateur_EmailOrderByDateDeDemandeDesc(String email);

    // Compte les réservations actives (EN_ATTENTE ou DISPO) pour un utilisateur
    long countByUtilisateur_EmailAndStatutNot(String email, StatutResa statut);


}
