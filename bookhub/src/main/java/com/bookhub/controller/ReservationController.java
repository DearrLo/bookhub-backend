package com.bookhub.controller;

import com.bookhub.bll.ReservationService;
import com.bookhub.bo.Reservation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@AllArgsConstructor
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private ReservationService reservationService;

    @GetMapping("/my")
    public ResponseEntity<List<Reservation>> listeReservationsparUtilisateur() {

        String emailConnecte = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Reservation> mesResas = reservationService.trouverParEmail(emailConnecte);
        if (mesResas == null || mesResas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(mesResas);
    }


    @PostMapping
    public ResponseEntity<?> ajoutReservation(@Valid @RequestBody Reservation reservation) {
        try {
            reservationService.reserverLivre(reservation);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> supprimerReservation(@PathVariable("id") String idInPath) {
        try {
            final int idResa = Integer.parseInt(idInPath);
            reservationService.supprimer(idResa);
            return ResponseEntity.ok("Réservation (" + idResa + ") est supprimée");
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Votre identifiant n'est pas un entier");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

}
