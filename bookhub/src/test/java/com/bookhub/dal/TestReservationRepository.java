package com.bookhub.dal;

import com.bookhub.bo.*;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@DataJpaTest
public class TestReservationRepository {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    private Utilisateur utilisateurDB;
    private Livre livreDB;

    @BeforeEach
    public void before() {
        final Utilisateur entiteUtilisateur = Utilisateur.builder()
                .email("solena8@gmail.com")
                .nom("Toussaint")
                .prenom("Soléna")
                .pseudo("soso")
                .motDePasse("So0solena8ol!")
                .role("USER")
                .build();

        final Categorie entiteCategorie = Categorie.builder()
                .libelle("Fiction")
                .build();

        final Categorie cat = categorieRepository.save(entiteCategorie);

        utilisateurDB = entityManager.persistFlushFind(entiteUtilisateur);

        final Livre entiteLivre = Livre.builder()
                .isbn("1ANHY6LGJTUEZKSHG")
                .titre("Le super livre")
                .auteur("Bob Razowski")
                .stock(8)
                .categorie(cat)
                .resume("Une aventure épique")
                .urlImage("https://exemple.com/image.jpg")
                .dateDeCreation(LocalDateTime.now())
                .build();

        livreDB = entityManager.persistFlushFind(entiteLivre);
    }

    @Test
    public void test_save_reservation_correct() {
        final Reservation entiteReservation = Reservation.builder()
                .dateDeDemande(LocalDateTime.now())
                .statut(StatutResa.EN_ATTENTE)
                .utilisateur(utilisateurDB)
                .livre(livreDB)
                .build();

        final Reservation sauvegarde = reservationRepository.save(entiteReservation);

        assertThat(sauvegarde).isNotNull();
        assertThat(sauvegarde.getId()).isGreaterThan(0);
        log.info(sauvegarde.toString());
    }

    @Test
    public void test_save_reservation_sans_date_de_demande() {
        final Reservation entiteReservation = Reservation.builder()
                .statut(StatutResa.EN_ATTENTE)
                .utilisateur(utilisateurDB)
                .livre(livreDB)
                .build();

        assertThrows(ConstraintViolationException.class, () -> {
            reservationRepository.save(entiteReservation);
            entityManager.flush();
        });
    }

    @Test
    public void test_save_reservation_sans_statut() {
        final Reservation entiteReservation = Reservation.builder()
                .dateDeDemande(LocalDateTime.now())
                .utilisateur(utilisateurDB)
                .livre(livreDB)
                .build();

        assertThrows(ConstraintViolationException.class, () -> {
            reservationRepository.save(entiteReservation);
            entityManager.flush();
        });
    }


    @Test
    public void test_delete() {
        final Reservation entiteReservation = Reservation.builder()
                .dateDeDemande(LocalDateTime.now())
                .statut(StatutResa.EN_ATTENTE)
                .utilisateur(utilisateurDB)
                .livre(livreDB)
                .build();

        final Reservation aSupprimer = reservationRepository.save(entiteReservation);
        Integer id = aSupprimer.getId();

        reservationRepository.delete(aSupprimer);

        entityManager.flush();
        entityManager.clear();

        Reservation resultat = entityManager.find(Reservation.class, id);
        assertNull(resultat);
    }
}