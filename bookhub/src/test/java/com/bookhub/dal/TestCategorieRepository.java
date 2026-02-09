package com.bookhub.dal;

import com.bookhub.bo.Categorie;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@DataJpaTest
public class TestCategorieRepository {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategorieRepository categorieRepository;


    @Test
    public void test_save_categorie_correct() {
        final Categorie entiteCategorie = Categorie.builder()
                .libelle("Fiction")
                .build();

        final Categorie sauvegarde = categorieRepository.save(entiteCategorie);

        assertThat(sauvegarde).isNotNull();
        assertThat(sauvegarde.getLibelle()).isEqualTo("Fiction");
        assertThat(sauvegarde.getId()).isGreaterThan(0);
        log.info(sauvegarde.toString());
    }

    @Test
    public void test_save_categorie_sans_libelle() {
        final Categorie entiteCategorie = Categorie.builder()
                         .build();

        assertThrows(ConstraintViolationException.class, () -> {
            categorieRepository.save(entiteCategorie);
            entityManager.flush();
        });
    }



    @Test
    public void test_delete() {
        final Categorie entiteCategorie = Categorie.builder()
                .libelle("Fiction")
                .build();

        final Categorie aSupprimer = categorieRepository.save(entiteCategorie);
        Integer id = aSupprimer.getId();

        categorieRepository.delete(aSupprimer);

        entityManager.flush();
        entityManager.clear();

        Categorie resultat = entityManager.find(Categorie.class, id);
        assertNull(resultat);
    }
}