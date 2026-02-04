package com.bookhub.dal;

import com.bookhub.bo.Emprunt;
import com.bookhub.bo.Reservation;
import com.bookhub.bo.Statut;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
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

//    private Utilisateur utilisateurDB;
//    private Livre livreDB;
//
//    @BeforeEach
//    public void before() {
//        final Utilisateur entiteUtilisateur = Utilisateur.builder()
//                .email("solena8@gmail.com")
//                .nom("Toussaint")
//                .prenom("Soléna")
//                .motDePasse("solena8")
//                .autorisations("LECTEUR")
//                .build();
//
//        utilisateurDB = entityManager.persistFlushFind(entiteUtilisateur);
//
//        final Livre entiteLivre = Livre.builder()
//                .isbn("1ANHY6LGJTUEZKSHG")
//                .titre("Le super livre")
//                .auteur("Bob Razowski")
//                .stock(8)
//                .resume("Une aventure épique")
//                .urlImage("https://exemple.com/image.jpg")
//                .dateDeCreation(LocalDateTime.now())
//                .build();
//
//        livreDB = entityManager.persistFlushFind(entiteLivre);
//    }

    @Test
    public void test_save_reservation_correct() {
        final Reservation entiteReservation = Reservation.builder()
                .dateDeDemande(LocalDateTime.now())
                .statut(Statut.EN_ATTENTE)
//                .utilisateur(utilisateurDB)
//                .livre(livreDB)
                .build();

        final Reservation sauvegarde = reservationRepository.save(entiteReservation);

        assertThat(sauvegarde).isNotNull();
        assertThat(sauvegarde.getId()).isGreaterThan(0);
        log.info(sauvegarde.toString());
    }

    @Test
    public void test_save_reservation_sans_date_de_demande() {
        final Reservation entiteReservation = Reservation.builder()
                .statut(Statut.EN_ATTENTE)
//                .utilisateur(utilisateurDB)
//                .livre(livreDB)
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
//                .utilisateur(utilisateurDB)
//                .livre(livreDB)
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
                .statut(Statut.EN_ATTENTE)
//                .utilisateur(utilisateurDB)
//                .livre(livreDB)
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