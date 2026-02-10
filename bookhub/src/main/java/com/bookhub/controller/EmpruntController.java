package com.bookhub.controller;


import com.bookhub.bll.EmpruntService;
import com.bookhub.bo.Emprunt;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/loans")
public class EmpruntController {

    private EmpruntService empruntService;

    @GetMapping
    public ResponseEntity<?> rechercherTousEmpruntsActifs() {
        final List<Emprunt> emprunts = empruntService.affficherEmpruntsActifs();

        if (emprunts == null || emprunts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(emprunts);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Emprunt>> listeEmpruntsparUtilisateur() {

        String emailConnecte = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Emprunt> mesEmprunts = empruntService.afficherHistoriqueDEmprunts(emailConnecte);
        if (mesEmprunts == null || mesEmprunts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(mesEmprunts);
    }


    @PostMapping
    public ResponseEntity<?> ajoutEmprunt(@Valid @RequestBody Emprunt emprunt) {
        try {
            empruntService.emprunterLivre(emprunt);
            return ResponseEntity.status(HttpStatus.CREATED).body(emprunt);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<?> miseAJourEmprunt(@PathVariable("id") Integer id) {
        try {
            Emprunt emprunt = new Emprunt();
            emprunt.setId(id);
            empruntService.validerRetourLivre(emprunt);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }
}
