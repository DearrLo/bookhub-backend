package com.bookhub.controller;

import com.bookhub.bll.LivreService;
import com.bookhub.bo.Livre;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/books")
public class LivreController {

    private LivreService livreService;

    /**
     * API GET : Récupère tous les livres du catalogue.
     * @return 200 OK avec la liste ou 204 No Content si vide.
     */
    @GetMapping
    public ResponseEntity<?> rechercherTousLivres() {
        final List<Livre> livres = livreService.afficherLivres();

        if (livres == null || livres.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(livres);
    }

    /**
     * API GET : Récupère le détail d'un livre par son identifiant.
     * @param idInPath L'identifiant passé dans l'URL.
     * @return 200 OK, 404 Not Found si inexistant, ou 406 Not Acceptable si l'ID n'est pas un entier.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> rechercherLivreParId(@PathVariable("id") String idInPath) {

        try {
            int id = Integer.parseInt(idInPath);
            final Livre livre = livreService.afficherDetailLivre(id);
            if (livre == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livre introuvable");
            }
            return ResponseEntity.ok(livre);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Votre identifiant n'est pas un entier");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * API POST : Ajoute un nouveau livre au catalogue.
     * @param livre Les données du livre validées par @Valid.
     * @return 201 Created avec l'objet créé ou 406 en cas d'erreur de validation.
     */
    @PostMapping
    public ResponseEntity<?> ajoutLivre(@Valid @RequestBody Livre livre) {
        try {
            livreService.ajouter(livre);
            return ResponseEntity.status(HttpStatus.CREATED).body(livre);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(e.getMessage());
        }
    }

    /**
     * API PUT : Met à jour les informations d'un livre existant.
     * @param livre Les nouvelles données du livre.
     * @return 200 OK avec le livre modifié ou 406 si les données sont invalides.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> miseAJourLivre(@Valid @RequestBody Livre livre) {
        try {
            if (livre == null || livre.getId() == null || livre.getId() <= 0) {
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body("Le livre et l'identifiant sont obligatoires");
            }
            livreService.modifier(livre);
            return ResponseEntity.ok(livre);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLivre(@PathVariable("id") String idInPath) {
        try {
            final int idLivre = Integer.parseInt(idInPath);
            livreService.supprimer(idLivre);
            return ResponseEntity.ok("Livre (" + idLivre + ") est supprimé");
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Votre identifiant n'est pas un entier");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> chercherLivres(
            @RequestParam(required = false) String mot_recherche) {
        try {
            final List<Livre> resultats = livreService.rechercherLivres(mot_recherche);
            if (resultats == null || resultats.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(resultats);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }


}
