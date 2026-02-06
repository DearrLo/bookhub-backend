package com.bookhub.dal;

import com.bookhub.bo.Utilisateur;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
public class TestUtilisateurRepository {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Test
    public void test_save_utilisateur_correct() {
        Utilisateur utilisateur = Utilisateur.builder()
                .email("test@bookhub.fr")
                .nom("Dupont")
                .prenom("Jean")
                .motDePasse("P@ssword123")
                .role("LECTEUR")
                .build();

        Utilisateur sauvegarde = utilisateurRepository.save(utilisateur);

        assertThat(sauvegarde).isNotNull();
        assertThat(sauvegarde.getEmail()).isEqualTo("test@bookhub.fr");
        log.info("Utilisateur sauvegardé : " + sauvegarde);
    }

    @Test
    public void test_existsByEmail() {
        Utilisateur utilisateur = Utilisateur.builder()
                .email("verif@bookhub.fr")
                .nom("Nom")
                .prenom("Prenom")
                .motDePasse("123456")
                .role("BIBLIOTHECAIRE")
                .build();
        entityManager.persistFlushFind(utilisateur);

        boolean existe = utilisateurRepository.existsByEmail("verif@bookhub.fr");
        boolean existePas = utilisateurRepository.existsByEmail("inconnu@bookhub.fr");

        assertThat(existe).isTrue();
        assertThat(existePas).isFalse();
    }
}