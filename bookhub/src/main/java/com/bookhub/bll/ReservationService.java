package com.bookhub.bll;

import com.bookhub.bo.Reservation;
import com.bookhub.bo.Statut;

public interface ReservationService {

    Reservation reserverLivre(Reservation reservation);

    Reservation mettreAJourStatut(Integer idReservation, Statut nouveauStatut);

    void annulerReservation(Integer idLivre, String emailUtilisateur);
}
