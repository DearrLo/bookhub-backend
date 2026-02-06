package com.bookhub.bll;

import com.bookhub.bo.*;
import com.bookhub.dal.EmpruntRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class EmpruntServiceImpl implements EmpruntService {

    private EmpruntRepository empruntRepository;

    @Override
    public List<Emprunt> affficherEmpruntsActifs() {
        return empruntRepository.findByStatutNot(StatutEmprunt.RENDU);
    }

    private void validerEmprunt(Emprunt emprunt) {
        if (emprunt == null) throw new RuntimeException("L'emprunt est obligatoire");
        if (emprunt.getLivre() == null) throw new RuntimeException("Le livre est obligatoire");

        Utilisateur lecteur = emprunt.getUtilisateur();
        if (lecteur == null) throw new RuntimeException("Le lecteur est obligatoire");

        long nbEmpruntsActifs = empruntRepository.countByUtilisateur_EmailAndDateDeRetourEffectiveIsNull(lecteur.getEmail());
        if (nbEmpruntsActifs >= 3) {
            throw new RuntimeException("Limite de 3 emprunts actifs atteinte.");
        }

        if (emprunt.getLivre().getStock() <= 0) {
            throw new RuntimeException("Stock épuisé pour ce livre.");
        }
    }

    @Transactional
    @Override
    public Emprunt emprunterLivre(Emprunt emprunt) {
        validerEmprunt(emprunt);
        emprunt.setStatut(StatutEmprunt.EMPRUNTE);
        Livre livre = emprunt.getLivre();
        livre.setStock(livre.getStock() - 1);
        emprunt.setDateDEmprunt(LocalDateTime.now());
        emprunt.setDateDeRetourAttendue(LocalDateTime.now().plusDays(14));
        return empruntRepository.save(emprunt);
    }



    @Transactional
    @Override
    public void validerRetourLivre(Emprunt emprunt) {
        emprunt.setStatut(StatutEmprunt.RENDU);
        emprunt.setDateDeRetourEffective(LocalDateTime.now());
        Livre livre = emprunt.getLivre();
        livre.setStock(livre.getStock() + 1);

        empruntRepository.save(emprunt);
    }

    @Override
    public List<Emprunt> afficherHistoriqueDEmprunts(String emailLecteur) {
        return empruntRepository.findByUtilisateur_EmailOrderByDateDEmpruntDesc(emailLecteur);
    }
}