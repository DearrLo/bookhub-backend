package com.bookhub.bll;

import com.bookhub.bo.Reservation;
import com.bookhub.bo.Statut;
import com.bookhub.dal.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService{
    private ReservationRepository reservationRepository;

    @Override
    public void reserverLivre(Reservation reservation) {
        if (reservation == null) {
            throw new RuntimeException("La réservation 'est pas renseignée");
        }

        try {
            reservationRepository.save(reservation);
        } catch (RuntimeException e) {
            throw new RuntimeException("Impossible de sauvegarder : " + reservation.toString());
        }

    }

    @Override
    public void annulerReservation(int idLivre, int idLecteur) {
        Reservation reservation = reservationRepository.findByLivreIdAndUtilisateurId(idLivre, idLecteur)
                .orElseThrow(() -> new RuntimeException("Réservation introuvable"));
        reservation.setStatut(Statut.ANNULEE);
        reservationRepository.save(reservation);
    }
}
