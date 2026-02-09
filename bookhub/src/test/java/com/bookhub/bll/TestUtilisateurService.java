package com.bookhub.bll;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bookhub.bo.Utilisateur;
import com.bookhub.dal.UtilisateurRepository;

@SpringBootTest
public class TestUtilisateurService {

    private UtilisateurService utilisateurService;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private PasswordEncoder passwordEncoder; // ajout car maintenant UtilisateurServiceImpl en dépend

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        utilisateurService = new UtilisateurServiceImpl(utilisateurRepository, passwordEncoder);
    }

    @Test //ajout du mdp dans le test
    void test_ajouterUtilisateur() {
        Utilisateur u = Utilisateur.builder()
                .email("new@test.fr")
                .motDePasse("mdp")
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
                .prenom("Soléna")
                .role("LECTEUR")
                .build();

        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateurModifie);

        Utilisateur resultat = utilisateurService.modifierUtilisateur(utilisateurModifie);

        assertThat(resultat).isNotNull();
        assertThat(resultat.getNom()).isEqualTo("Toussaint-Modifie");
        verify(utilisateurRepository, times(1)).save(utilisateurModifie);
    }
}
