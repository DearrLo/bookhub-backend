package com.bookhub.bll;

import com.bookhub.bo.Livre;
import com.bookhub.dal.LivreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TestLivreService {

    private LivreService livreService;

    @Mock
    private LivreRepository livreRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        livreService = new LivreServiceImpl(livreRepository);
    }

    @Test
    void test_afficherLivres() {
        when(livreRepository.findAll()).thenReturn(Arrays.asList(new Livre(), new Livre()));

        List<Livre> resultats = livreService.afficherLivres();

        assertThat(resultats.size()).isEqualTo(2);
        verify(livreRepository).findAll();
    }

    @Test
    void test_chargerLivreParDisponibilite() {
        livreService.chargerLivreParDisponibilite();
        verify(livreRepository).findByStockGreaterThan(0);
    }

    @Test
    void test_afficherDetailLivre_trouve() {
        Livre livre = Livre.builder().id(1).build();
        when(livreRepository.findById(1)).thenReturn(Optional.of(livre));

        Livre resultat = livreService.afficherDetailLivre(1);

        assertThat(resultat).isNotNull();
    }

    @Test
    void test_afficherDetailLivre_non_trouve() {
        when(livreRepository.findById(99)).thenReturn(Optional.empty());
        Livre resultat = livreService.afficherDetailLivre(99);
        assertThat(resultat).isNull();
    }

    @Test
    void test_ajouter_livre_Success() {
        Livre nouveauLivre = Livre.builder()
                .titre("Le Grimoire de Spring")
                .auteur("Soléna")
                .stock(10)
                .build();
        when(livreRepository.save(any(Livre.class))).thenReturn(nouveauLivre);
        Livre resultat = livreService.ajouter(nouveauLivre);
        assertThat(resultat).isNotNull();
        assertThat(resultat.getTitre()).isEqualTo("Le Grimoire de Spring");
        verify(livreRepository, times(1)).save(nouveauLivre);
    }

    @Test
    void test_modifier_livre() {
        Livre livreExistant = Livre.builder()
                .id(1)
                .titre("Ancien Titre")
                .auteur("Auteur")
                .stock(10)
                .build();

        Livre livreModifie = Livre.builder()
                .id(1)
                .titre("Nouveau Titre")
                .build();

        when(livreRepository.findById(1)).thenReturn(Optional.of(livreExistant));
        when(livreRepository.save(any(Livre.class))).thenAnswer(i -> i.getArguments()[0]);
        Livre resultat = livreService.modifier(livreModifie);
        assertThat(resultat.getTitre()).isEqualTo("Nouveau Titre");
        assertThat(resultat.getAuteur()).isEqualTo("Auteur");
        verify(livreRepository).save(any(Livre.class));
    }

    @Test
    void test_chargerLivreParCategorie() {
        Integer catId = 10;
        livreService.chargerLivreParCategorie(catId);
        verify(livreRepository).findByCategorie_Id(catId);
    }

    @Test
    void test_supprimer_livre() {
        livreService.supprimer(1);
        verify(livreRepository).deleteById(1);
    }
}