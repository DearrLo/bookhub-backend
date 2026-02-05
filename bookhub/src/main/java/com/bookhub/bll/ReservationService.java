package com.bookhub.bll;

import com.bookhub.bo.Reservation;
import com.bookhub.bo.StatutResa;

public interface ReservationService {

    Reservation reserverLivre(Reservation reservation);

    Reservation mettreAJourStatut(Integer idReservation, StatutResa nouveauStatut);

    void annulerReservation(Integer idLivre, String emailUtilisateur);
}
