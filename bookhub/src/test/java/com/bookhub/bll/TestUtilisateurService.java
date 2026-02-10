package com.bookhub.bll;

import com.bookhub.bo.Utilisateur;
import com.bookhub.dal.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TestUtilisateurService {

    private UtilisateurService utilisateurService;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private MessageSource messageSource;

    @Mock
    private PasswordEncoder passwordEncoder; // ajout car maintenant UtilisateurServiceImpl en dépend

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        utilisateurService = new UtilisateurServiceImpl(utilisateurRepository, messageSource, passwordEncoder);
    }

    @Test
    void test_ajouterUtilisateur() {
        Utilisateur u = Utilisateur.builder()
                .email("new@test.fr")
                .motDePasse("mdp")
                .nom("LALALA")
                .role("USER")
                .prenom("Bob")
                .build();

        when(passwordEncoder.encode("mdp")).thenReturn("mdp_test");

        utilisateurService.ajouterUtilisateur(u);

        assertThat(u.getMotDePasse()).isEqualTo("mdp_test");
        verify(utilisateurRepository).save(u);
    }

    @Test
    void test_supprimerUtilisateur() {
        String email = "test@test.fr";
        utilisateurService.supprimerUtilisateur(email);
        verify(utilisateurRepository).deleteById(email);
    }

    @Test
    void test_modifierUtilisateur_ok() {
        Utilisateur utilisateurModifie = Utilisateur.builder()
                .email("solena8@gmail.com")
                .nom("Toussaint-Modifie")
                .build();

        when(utilisateurRepository.findById("solena8@gmail.com")).thenReturn(Optional.of(utilisateurModifie));
        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateurModifie);

        Utilisateur resultat = utilisateurService.modifierUtilisateur(utilisateurModifie);

        assertThat(resultat).isNotNull();
        assertThat(resultat.getNom()).isEqualTo("Toussaint-Modifie");
    }
}
