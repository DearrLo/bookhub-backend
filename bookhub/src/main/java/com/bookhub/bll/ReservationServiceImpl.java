package com.bookhub.bll;

import com.bookhub.bo.Livre;
import com.bookhub.bo.Reservation;
import com.bookhub.bo.StatutResa;
import com.bookhub.bo.Utilisateur;
import com.bookhub.dal.LivreRepository;
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
    private LivreRepository livreRepository;
    private MessageSource messageSource;

    /**
     * Récupère l'historique des réservations d'un utilisateur spécifique.
     * Les résultats sont triés par date de demande décroissante.
     * @param email L'adresse email de l'utilisateur concerné.
     * @return Une liste de réservations.
     */
    @Override
    public List<Reservation> trouverParEmail(String email) {
        return reservationRepository.findByUtilisateur_EmailOrderByDateDeDemandeDesc(email);
    }

    /**
     * Supprime une réservation par son identifiant unique.
     * @param id L'identifiant de la réservation.
     */
    @Override
    public void supprimer(Integer id) {
        reservationRepository.deleteById(id);
    }

    /**
     * Applique les règles métier pour valider une demande de réservation.
     * Vérifie l'existence du livre, de l'utilisateur, et que l'utilisateur
     * n'a pas dépassé la limite maximale de réservations actives.
     * @param reservation L'objet réservation à valider.
     * @throws RuntimeException si les conditions de réservation ne sont pas remplies.
     */
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
        if (reservation.getLivre().getStock() != null && reservation.getLivre().getStock() > 0) {
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

    /**
     * Crée une nouvelle réservation avec le statut "EN_ATTENTE".
     * @param reservation Les données de la réservation.
     * @return La réservation enregistrée.
     */
    @Override
    public Reservation reserverLivre(Reservation reservation) {
        Livre livreBdd = livreRepository.findById(reservation.getLivre().getId())
                .orElseThrow(() -> new RuntimeException("Livre introuvable"));

        reservation.setLivre(livreBdd);

        reservation.setDateDeDemande(LocalDateTime.now());
        reservation.setStatut(StatutResa.EN_ATTENTE);

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

    /**
     * Annule une réservation active en changeant son statut.
     * @param idLivre L'id du livre concerné.
     * @param emailUtilisateur L'email du demandeur.
     */
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
