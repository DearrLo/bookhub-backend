package com.bookhub.dal;

import com.bookhub.bo.Reservation;
import com.bookhub.bo.StatutResa;
import com.bookhub.bo.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    Optional<Reservation> findByLivreIdAndUtilisateurEmail(Integer idLivre, String emailUtilisateur);

    List<Reservation> findByUtilisateur(Utilisateur utilisateur);

    // Compte les réservations actives (EN_ATTENTE ou DISPO) pour un utilisateur
    long countByUtilisateur_EmailAndStatutNot(String email, StatutResa statut);


}
