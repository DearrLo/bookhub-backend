package com.bookhub.bll;

import com.bookhub.bo.Reservation;

public interface ReservationService {

    void reserverLivre(Reservation reservation);
    void annulerReservation(int idLivre, int idLecteur);
}
