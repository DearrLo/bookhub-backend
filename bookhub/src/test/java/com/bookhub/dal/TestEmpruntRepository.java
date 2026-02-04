package com.bookhub.dal;

import com.bookhub.bo.Commentaire;
import com.bookhub.bo.Emprunt;
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
public class TestEmpruntRepository {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmpruntRepository empruntRepository;

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
    public void test_save_emprunt_correct() {
        LocalDateTime dateFixe = LocalDateTime.now();
        final Emprunt entiteEmprunt = Emprunt.builder()
                .dateDEmprunt(dateFixe)
                .dateDeRetourAttendue(dateFixe)
                .dateDeRetourEffective(dateFixe)
//                .utilisateur(utilisateurDB)
//                .livre(livreDB)
                .build();

        final Emprunt sauvegarde = empruntRepository.save(entiteEmprunt);

        assertThat(sauvegarde).isNotNull();
        assertThat(sauvegarde.getDateDEmprunt()).isEqualTo(dateFixe);
        assertThat(sauvegarde.getId()).isGreaterThan(0);
        log.info(sauvegarde.toString());
    }

    @Test
    public void test_save_emprunt_sans_date_d_emprunt() {
        final Emprunt entiteEmprunt = Emprunt.builder()
                .dateDeRetourAttendue(LocalDateTime.now())
                .dateDeRetourEffective(LocalDateTime.now())
//                .utilisateur(utilisateurDB)
//                .livre(livreDB)
                .build();

        assertThrows(ConstraintViolationException.class, () -> {
            empruntRepository.save(entiteEmprunt);
            entityManager.flush();
        });
    }


    @Test
    public void test_delete() {
            final Emprunt entiteEmprunt = Emprunt.builder()
                    .dateDEmprunt(LocalDateTime.now())
                    .dateDeRetourAttendue(LocalDateTime.now())
                    .dateDeRetourEffective(LocalDateTime.now())
//                .utilisateur(utilisateurDB)
//                .livre(livreDB)
                    .build();

        final Emprunt aSupprimer = empruntRepository.save(entiteEmprunt);
        Integer id = aSupprimer.getId();

        empruntRepository.delete(aSupprimer);

        entityManager.flush();
        entityManager.clear();

        Emprunt resultat = entityManager.find(Emprunt.class, id);
        assertNull(resultat);
    }
}