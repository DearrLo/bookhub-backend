package com.bookhub.bll;

import com.bookhub.bo.Commentaire;
import com.bookhub.bo.Livre;
import com.bookhub.bo.Utilisateur;
import com.bookhub.dal.CommentaireRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TestCommentaireService {

    private CommentaireService commentaireService;

    @Mock
    private CommentaireRepository commentaireRepository;

    private Utilisateur utilisateurTest;
    private Livre livreTest;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        commentaireService = new CommentaireServiceImpl(commentaireRepository);

        utilisateurTest = Utilisateur.builder().email("test@eni.fr").build();
        livreTest = Livre.builder().id(1).titre("Livre Test").build();
    }

    @Test
    void test_laisserCommentaire_ok() {
        Commentaire com = Commentaire.builder()
                .utilisateur(utilisateurTest)
                .livre(livreTest)
                .note(4)
                .commentaire("Super livre !")
                .build();

        when(commentaireRepository.save(any(Commentaire.class))).thenReturn(com);

        Commentaire resultat = commentaireService.laisserCommentaire(com);

        assertThat(resultat).isNotNull();
        assertThat(resultat.getDateDeCreation()).isNotNull();
        verify(commentaireRepository, times(1)).save(com);
    }

    @Test
    void test_laisserCommentaire_note_invalide() {
        Commentaire com = Commentaire.builder()
                .utilisateur(utilisateurTest)
                .livre(livreTest)
                .commentaire("top")
                .note(10) // Note trop haute
                .build();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commentaireService.laisserCommentaire(com);
        });

        assertThat(exception.getMessage()).isEqualTo("La note doit être comprise entre 0 et 5");
        verify(commentaireRepository, never()).save(any());
    }

    @Test
    void test_laisserCommentaire_erreur_sauvegarde() {
        Commentaire com = Commentaire.builder()
                .utilisateur(utilisateurTest).livre(livreTest).note(3).build();

        // On simule une erreur lors du save()
        when(commentaireRepository.save(any())).thenThrow(new RuntimeException("Erreur DB"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            commentaireService.laisserCommentaire(com);
        });

        assertThat(exception.getMessage()).contains("Impossible de sauver");
    }

    @Test
    void test_supprimerCommentaire() {
        Integer id = 1;
        commentaireService.supprimerCommentaire(id);
        // On vérifie que deleteById a bien été appelé
        verify(commentaireRepository, times(1)).deleteById(id);
    }
}