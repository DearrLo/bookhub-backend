package com.bookhub.dal;

import com.bookhub.bo.Commentaire;
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
public class TestCommentaireRepository {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentaireRepository commentaireRepository;

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
//                .autorisations("USER")
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
    public void test_save_commentaire_correct() {
        final Commentaire entiteCommentaire = Commentaire.builder()
                .commentaire("j'ai beaucoup aimé")
                .note(5)
                .dateDeCreation(LocalDateTime.now())
//                .utilisateur(utilisateurDB)
//                .livre(livreDB)
                .build();

        final Commentaire sauvegarde = commentaireRepository.save(entiteCommentaire);

        assertThat(sauvegarde).isNotNull();
        assertThat(sauvegarde.getCommentaire()).isEqualTo("j'ai beaucoup aimé");
        assertThat(sauvegarde.getId()).isGreaterThan(0);
        log.info(sauvegarde.toString());
    }

    @Test
    public void test_save_commentaire_avec_note_superieure_a_5() {
        final Commentaire entiteCommentaire = Commentaire.builder()
                .commentaire("j'ai beaucoup aimé")
                .note(7)
                .dateDeCreation(LocalDateTime.now())
//                .utilisateur(utilisateurDB)
//                .livre(livreDB)
                .build();

        assertThrows(ConstraintViolationException.class, () -> {
            commentaireRepository.save(entiteCommentaire);
            entityManager.flush();
        });
    }

    @Test
    public void test_save_commentaire_sans_note() {
        final Commentaire entiteCommentaire = Commentaire.builder()
                .commentaire("j'ai beaucoup aimé")
                .dateDeCreation(LocalDateTime.now())
//                .utilisateur(utilisateurDB)
//                .livre(livreDB)
                .build();

        assertThrows(ConstraintViolationException.class, () -> {
            commentaireRepository.save(entiteCommentaire);
            entityManager.flush();
        });
    }

    @Test
    public void test_delete() {
        final Commentaire entiteCommentaire = Commentaire.builder()
                .commentaire("Commentaire éphémère")
                .note(3)
                .dateDeCreation(LocalDateTime.now())
//                .utilisateur(utilisateurDB)
//                .livre(livreDB)
                .build();

        final Commentaire aSupprimer = commentaireRepository.save(entiteCommentaire);
        Integer id = aSupprimer.getId();

        commentaireRepository.delete(aSupprimer);

        entityManager.flush();
        entityManager.clear();

        Commentaire resultat = entityManager.find(Commentaire.class, id);
        assertNull(resultat);
    }
}