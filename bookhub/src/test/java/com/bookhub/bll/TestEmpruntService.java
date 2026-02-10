package com.bookhub.bll;

import com.bookhub.bo.Emprunt;
import com.bookhub.bo.Livre;
import com.bookhub.bo.StatutEmprunt;
import com.bookhub.bo.Utilisateur;
import com.bookhub.dal.EmpruntRepository;
import com.bookhub.dal.LivreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TestEmpruntService {

    private EmpruntService empruntService;

    @Mock
    private EmpruntRepository empruntRepository;

    @Mock
    private LivreRepository livreRepository;

    @Mock
    private MessageSource messageSource;

    private Utilisateur lecteur;
    private Livre livre;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        when(messageSource.getMessage(anyString(), any(), any(Locale.class))).thenReturn("message test");
        empruntService = new EmpruntServiceImpl(empruntRepository, livreRepository, messageSource);

        lecteur = Utilisateur.builder().email("solena@test.fr").build();
        livre = Livre.builder().id(1).stock(5).build();

        when(livreRepository.findById(anyInt())).thenReturn(Optional.of(livre));
    }

    @Test
    void test_demandeEmprunt_limite_atteinte() {
        when(empruntRepository.countByUtilisateur_EmailAndDateDeRetourEffectiveIsNull(lecteur.getEmail()))
                .thenReturn(3L);

        when(messageSource.getMessage(eq("loan.limit.reached"), any(), any(Locale.class)))
                .thenReturn("Limite de 3 emprunts actifs atteinte.");

        Emprunt nouvelEmprunt = Emprunt.builder().utilisateur(lecteur).livre(livre).build();

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            empruntService.emprunterLivre(nouvelEmprunt);
        });

        assertThat(ex.getMessage()).isEqualTo("Limite de 3 emprunts actifs atteinte.");
    }

    @Test
    void test_validerEmprunt_diminue_stock() {
        Emprunt emprunt = Emprunt.builder().livre(livre).utilisateur(lecteur).build();
        int stockInitial = livre.getStock();

        empruntService.emprunterLivre(emprunt);

        assertThat(emprunt.getStatut()).isEqualTo(StatutEmprunt.EMPRUNTE);
        assertThat(livre.getStock()).isEqualTo(stockInitial - 1);
        verify(empruntRepository).save(emprunt);
    }

    @Test
    void test_validerRetour_augmente_stock() {
        Emprunt emprunt = Emprunt.builder().id(10).livre(livre).utilisateur(lecteur).build();
        int stockAvantRetour = livre.getStock();

        when(empruntRepository.findById(10)).thenReturn(java.util.Optional.of(emprunt));

        empruntService.validerRetourLivre(emprunt);

        assertThat(emprunt.getStatut()).isEqualTo(StatutEmprunt.RENDU);
        assertThat(emprunt.getDateDeRetourEffective()).isNotNull();
        assertThat(livre.getStock()).isEqualTo(stockAvantRetour + 1);
        verify(empruntRepository).save(emprunt);
    }

    @Test
    void test_RendreLivre() {
        Emprunt emprunt = Emprunt.builder().id(1).livre(livre).build();

        when(empruntRepository.findById(1)).thenReturn(java.util.Optional.of(emprunt));

        empruntService.validerRetourLivre(emprunt);

        assertThat(emprunt.getStatut()).isEqualTo(StatutEmprunt.RENDU);
        verify(empruntRepository).save(emprunt);
    }

    @Test
    void test_afficherHistoriqueDEmprunts() {
        String email = "solena@test.fr";
        empruntService.afficherHistoriqueDEmprunts(email);

        verify(empruntRepository).findByUtilisateur_EmailOrderByDateDEmpruntDesc(email);
    }

    @Test
    void test_validerEmprunt_stock_epuise() {
        Livre livreSansStock = Livre.builder().id(2).stock(0).build();

        when(livreRepository.findById(2)).thenReturn(Optional.of(livreSansStock));

        Emprunt emprunt = Emprunt.builder().utilisateur(lecteur).livre(livreSansStock).build();

        when(messageSource.getMessage(eq("loan.not.available"), any(), any(Locale.class)))
                .thenReturn("Stock épuisé pour ce livre.");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            empruntService.emprunterLivre(emprunt);
        });

        assertThat(ex.getMessage()).isEqualTo("Stock épuisé pour ce livre.");
    }
}
