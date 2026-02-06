package com.bookhub.bll;

import com.bookhub.bo.Emprunt;
import com.bookhub.bo.Livre;
import com.bookhub.bo.StatutEmprunt;
import com.bookhub.bo.Utilisateur;
import com.bookhub.dal.EmpruntRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TestEmpruntService {

    private EmpruntService empruntService;

    @Mock
    private EmpruntRepository empruntRepository;

    private Utilisateur lecteur;
    private Livre livre;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        empruntService = new EmpruntServiceImpl(empruntRepository);

        lecteur = Utilisateur.builder().email("lecteur@test.fr").build();
        livre = Livre.builder().id(1).stock(5).build();
    }

    @Test
    void test_demandeEmprunt_limite_atteinte() {
        // Simule déjà 3 emprunts en cours
        when(empruntRepository.countByUtilisateur_EmailAndDateDeRetourEffectiveIsNull(lecteur.getEmail()))
                .thenReturn(3L);

        Emprunt nouvelEmprunt = Emprunt.builder().utilisateur(lecteur).livre(livre).build();

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            empruntService.demandeEmpruntLivre(nouvelEmprunt);
        });

        assertThat(ex.getMessage()).isEqualTo("Limite de 3 emprunts actifs atteinte.");
    }

    @Test
    void test_validerEmprunt_diminue_stock() {
        Emprunt emprunt = Emprunt.builder().livre(livre).utilisateur(lecteur).build();
        int stockInitial = livre.getStock();

        empruntService.validerEmpruntLivre(emprunt);

        assertThat(emprunt.getStatut()).isEqualTo(StatutEmprunt.EMPRUNTE);
        assertThat(livre.getStock()).isEqualTo(stockInitial - 1);
        verify(empruntRepository).save(emprunt);
    }

    @Test
    void test_validerRetour_augmente_stock() {
        Emprunt emprunt = Emprunt.builder().livre(livre).utilisateur(lecteur).build();
        int stockAvantRetour = livre.getStock();

        empruntService.validerRetourLivre(emprunt);

        assertThat(emprunt.getStatut()).isEqualTo(StatutEmprunt.RENDU);
        assertThat(emprunt.getDateDeRetourEffective()).isNotNull();
        assertThat(livre.getStock()).isEqualTo(stockAvantRetour + 1);
        verify(empruntRepository).save(emprunt);
    }

    @Test
    void test_RendreLivre() {
        Emprunt emprunt = Emprunt.builder().build();
        empruntService.RendreLivre(emprunt);

        assertThat(emprunt.getStatut()).isEqualTo(StatutEmprunt.EN_DEMANDE_RETOUR);
        verify(empruntRepository).save(emprunt);
    }

    @Test
    void test_afficherHistoriqueDEmprunts() {
        String email = "solena@test.fr";
        empruntService.afficherHistoriqueDEmprunts(email);

        // Vérifie l'appel à la méthode spécifique du repository
        verify(empruntRepository).findByUtilisateur_EmailOrderByDateDEmpruntDesc(email);
    }

    @Test
    void test_validerEmprunt_stock_epuise() {
        Livre livreSansStock = Livre.builder().stock(0).build();
        Emprunt emprunt = Emprunt.builder().utilisateur(lecteur).livre(livreSansStock).build();

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            empruntService.demandeEmpruntLivre(emprunt);
        });

        assertThat(ex.getMessage()).isEqualTo("Stock épuisé pour ce livre.");
    }
}