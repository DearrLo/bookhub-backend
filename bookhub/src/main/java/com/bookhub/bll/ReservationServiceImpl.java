package com.bookhub.bll;

import com.bookhub.bo.Reservation;
import com.bookhub.bo.Statut;
import com.bookhub.dal.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService{

    private ReservationRepository reservationRepository;


    private void validerReservation(Reservation reservation){
        if (reservation == null) {
            throw new RuntimeException("La réservation est obligatoire");
        }
        if (reservation.getLivre() == null) {
            throw new RuntimeException("Le livre est obligatoire");
        }
        if (reservation.getUtilisateur() == null) {
            throw new RuntimeException("Le lecteur est obligatoire");
        }
        reservation.setStatut(Statut.EN_ATTENTE);
        reservation.setDateDeDemande(LocalDateTime.now());

    }

    @Override
    public Reservation reserverLivre(Reservation reservation) {
        validerReservation(reservation);
        try {
            return reservationRepository.save(reservation);
        } catch (RuntimeException e) {
            throw new RuntimeException("Impossible de sauver - " + reservation.toString());
        }
    }

    @Override
    public Reservation mettreAJourStatut(Integer idReservation, Statut nouveauStatut) {
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));
        reservation.setStatut(nouveauStatut);
        return reservationRepository.save(reservation);
    }



    @Override
    public void annulerReservation(Integer idLivre, String emailUtilisateur) {
        Reservation reservation = reservationRepository.findByLivreIdAndUtilisateurEmail(idLivre, emailUtilisateur)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));
        reservation.setStatut(Statut.ANNULEE);
        reservationRepository.save(reservation);
    }
}
