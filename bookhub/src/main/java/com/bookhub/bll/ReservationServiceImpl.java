package com.bookhub.bll;

import com.bookhub.bo.Reservation;
import com.bookhub.bo.StatutResa;
import com.bookhub.bo.Utilisateur;
import com.bookhub.dal.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@AllArgsConstructor
@Service
public class ReservationServiceImpl implements ReservationService {

    public static final int LIMITE_MAX_RESERVATIONS = 5;

    private ReservationRepository reservationRepository;
    private MessageSource messageSource;

    @Override
    public List<Reservation> trouverParEmail(String email) {
        return reservationRepository.findByUtilisateur_EmailOrderByDateDeDemandeDesc(email);
    }

    @Override
    public void supprimer(Integer id) {
        reservationRepository.deleteById(id);
    }

    private void validerReservation(Reservation reservation) {
        if (reservation == null) {
            throw new RuntimeException(messageSource.getMessage("reservation.required", null, Locale.getDefault()));
        }
        if (reservation.getLivre() == null) {
            throw new RuntimeException(messageSource.getMessage("book.required", null, Locale.getDefault()));
        }

        Utilisateur lecteur = reservation.getUtilisateur();
        if (lecteur == null) {
            throw new RuntimeException(messageSource.getMessage("user.required", null, Locale.getDefault()));
        }
        if (reservation.getLivre().getStock() > 0) {
            throw new RuntimeException(messageSource.getMessage("reservation.book.available", null, Locale.getDefault()));
        }
        long nbReservationsActives = reservationRepository.countByUtilisateur_EmailAndStatutNot(
                lecteur.getEmail(),
                StatutResa.ANNULEE
        );

        if (nbReservationsActives >= LIMITE_MAX_RESERVATIONS) {
            throw new RuntimeException(messageSource.getMessage("reservation.limit.reached", null, Locale.getDefault()));
        }

        reservation.setStatut(StatutResa.EN_ATTENTE);
        reservation.setDateDeDemande(LocalDateTime.now());
    }

    @Override
    public Reservation reserverLivre(Reservation reservation) {
        validerReservation(reservation);
        try {
            return reservationRepository.save(reservation);
        } catch (RuntimeException e) {
            throw new RuntimeException(messageSource.getMessage("reservation.save.failed", null, Locale.getDefault()));
        }
    }

    @Override
    public Reservation mettreAJourStatut(Integer idReservation, StatutResa nouveauStatut) {
        Reservation reservation = reservationRepository.findById(idReservation)
                .orElseThrow(() -> new RuntimeException(
                        messageSource.getMessage("reservation.not.found", null, Locale.getDefault())
                ));
        reservation.setStatut(nouveauStatut);
        return reservationRepository.save(reservation);
    }

    @Override
    public void annulerReservation(Integer idLivre, String emailUtilisateur) {
        Reservation reservation = reservationRepository.findByLivre_IdAndUtilisateur_Email(idLivre, emailUtilisateur)
                .orElseThrow(() -> new RuntimeException(
                        messageSource.getMessage("reservation.not.found", null, Locale.getDefault())
                ));
        reservation.setStatut(StatutResa.ANNULEE);
        reservationRepository.save(reservation);
    }
}
