package com.bookhub.bll;

import com.bookhub.bo.Livre;
import com.bookhub.bo.Reservation;
import com.bookhub.bo.StatutResa;
import com.bookhub.bo.Utilisateur;
import com.bookhub.dal.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TestReservationService {

    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    private Utilisateur utilisateurTest;
    private Livre livreTest;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);

        reservationService = new ReservationServiceImpl(reservationRepository);

        utilisateurTest = Utilisateur.builder()
                .email("test@eni.fr")
                .nom("Toussaint")
                .prenom("Soléna")
                .build();

        livreTest = Livre.builder()
                .id(1)
                .titre("Apprendre Spring Boot")
                .build();
    }

    @Test
    void test_reserverLivre_limite_atteinte() {
        when(reservationRepository.countByUtilisateur_EmailAndStatutNot(
                eq(utilisateurTest.getEmail()),
                eq(StatutResa.ANNULEE)
        )).thenReturn(5L);

        Reservation nouvelleResa = Reservation.builder()
                .utilisateur(utilisateurTest)
                .livre(livreTest)
                .build();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reservationService.reserverLivre(nouvelleResa);
        });

        assertThat(exception.getMessage()).isEqualTo("Limite de 5 réservations actives atteinte.");
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void test_reserverLivre_ok() {
        when(reservationRepository.countByUtilisateur_EmailAndStatutNot(anyString(), any()))
                .thenReturn(0L);

        Reservation resa = Reservation.builder()
                .utilisateur(utilisateurTest)
                .livre(livreTest)
                .build();

        when(reservationRepository.save(any(Reservation.class))).thenReturn(resa);

        Reservation resultat = reservationService.reserverLivre(resa);

        assertNotNull(resultat);
        assertThat(resultat.getStatut()).isEqualTo(StatutResa.EN_ATTENTE);
        assertThat(resultat.getDateDeDemande()).isNotNull();
        verify(reservationRepository, times(1)).save(resa);
    }

    @Test
    void test_mettreAJourStatut_Succes() {
        int idResa = 10;
        Reservation resaExistante = Reservation.builder()
                .id(idResa)
                .statut(StatutResa.EN_ATTENTE)
                .build();

        when(reservationRepository.findById(idResa)).thenReturn(Optional.of(resaExistante));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArguments()[0]);

        Reservation resultat = reservationService.mettreAJourStatut(idResa, StatutResa.DISPO);

        assertThat(resultat.getStatut()).isEqualTo(StatutResa.DISPO);
        verify(reservationRepository).findById(idResa);
        verify(reservationRepository).save(resaExistante);
    }

    @Test
    void test_annulerReservation_Succes() {
        Reservation resaAAnnuler = Reservation.builder()
                .livre(livreTest)
                .utilisateur(utilisateurTest)
                .statut(StatutResa.EN_ATTENTE)
                .build();

        when(reservationRepository.findByLivreIdAndUtilisateurEmail(1, "test@eni.fr"))
                .thenReturn(Optional.of(resaAAnnuler));

        reservationService.annulerReservation(1, "test@eni.fr");

        assertThat(resaAAnnuler.getStatut()).isEqualTo(StatutResa.ANNULEE);
        verify(reservationRepository).save(resaAAnnuler);
    }
}