package com.bookhub.dal;

import com.bookhub.bo.Categorie;
import com.bookhub.bo.Livre;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
public class TestLivreRepository {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LivreRepository livreRepository;

    private Categorie catFiction;

    @BeforeEach
    public void setup() {
        catFiction = Categorie.builder().libelle("Fiction").build();
        entityManager.persist(catFiction);

        Livre l1 = Livre.builder()
                .titre("Le Seigneur des Anneaux")
                .auteur("J.R.R. Tolkien")
                .isbn("123456")
                .stock(5)
                .categorie(catFiction)
                .dateDeCreation(LocalDateTime.now())
                .build();

        Livre l2 = Livre.builder()
                .titre("Le Hobbit")
                .auteur("J.R.R. Tolkien")
                .isbn("789012")
                .stock(0)
                .categorie(catFiction)
                .dateDeCreation(LocalDateTime.now())
                .build();

        entityManager.persist(l1);
        entityManager.persist(l2);
        entityManager.flush();
    }

    @Test
    public void test_findByTitreContaining() {
        List<Livre> resultats = livreRepository.findByTitreContainingIgnoreCase("Seigneur");
        assertThat(resultats.size()).isEqualTo(1);
        assertThat(resultats.get(0).getTitre()).contains("Anneaux");
    }

    @Test
    public void test_findByAuteur() {
        List<Livre> resultats = livreRepository.findByAuteurContainingIgnoreCase("Tolkien");
        assertThat(resultats.size()).isEqualTo(2);
    }

    @Test
    public void test_findByStockGreaterThan() {
        // On cherche les livres dont le stock > 0
        List<Livre> enStock = livreRepository.findByStockGreaterThan(0);
        assertThat(enStock.size()).isEqualTo(1);
        assertThat(enStock.get(0).getTitre()).isEqualTo("Le Seigneur des Anneaux");
    }

    @Test
    public void test_findByCategorie() {
        List<Livre> resultats = livreRepository.findByCategorie_Id(catFiction.getId());
        assertThat(resultats.isEmpty()).isFalse();
    }
}