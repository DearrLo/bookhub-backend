package com.bookhub.bll;

import com.bookhub.bo.Reservation;
import com.bookhub.bo.StatutResa;

import java.util.List;

public interface ReservationService {

    List<Reservation> trouverParEmail(String email);

    void supprimer(Integer id);

    Reservation reserverLivre(Reservation reservation);

    Reservation mettreAJourStatut(Integer idReservation, StatutResa nouveauStatut);

    void annulerReservation(Integer idLivre, String emailUtilisateur);
}
