package com.bookhub.dal;

import com.bookhub.bo.Emprunt;
import com.bookhub.bo.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

        Optional<Reservation> findByLivreIdAndUtilisateurId(Integer idLivre, Integer idUtilisateur);
}
