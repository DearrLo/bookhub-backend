package com.bookhub.bll;

import com.bookhub.bo.Utilisateur;
import com.bookhub.dal.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TestUtilisateurService {

    private UtilisateurService utilisateurService;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        utilisateurService = new UtilisateurServiceImpl(utilisateurRepository);
    }

    @Test
    void test_ajouterUtilisateur() {
        Utilisateur u = Utilisateur.builder().email("new@test.fr").build();
        utilisateurService.ajouterUtilisateur(u);
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