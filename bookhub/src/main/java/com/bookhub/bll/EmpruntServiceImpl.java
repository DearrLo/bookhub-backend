package com.bookhub.bll;

import com.bookhub.bo.Emprunt;
import com.bookhub.bo.Livre;
import com.bookhub.bo.StatutEmprunt;
import com.bookhub.bo.Utilisateur;
import com.bookhub.dal.EmpruntRepository;
import com.bookhub.dal.LivreRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class EmpruntServiceImpl implements EmpruntService {

    private EmpruntRepository empruntRepository;
    private LivreRepository livreRepository;
    private MessageSource messageSource;

    public static final int DUREE_EMPRUNT_JOURS = 14;
    public static final int LIMITE_MAX_EMPRUNTS = 3;

    @Override
    public List<Emprunt> affficherEmpruntsActifs() {
        return empruntRepository.findByStatutNot(StatutEmprunt.RENDU);
    }

    private void validerEmprunt(Emprunt emprunt) {
        if (emprunt == null) throw new RuntimeException(messageSource.getMessage("loan.required", null, Locale.getDefault()));
        if (emprunt.getLivre() == null) throw new RuntimeException(messageSource.getMessage("book.required", null, Locale.getDefault()));

        Utilisateur lecteur = emprunt.getUtilisateur();
        if (lecteur == null) throw new RuntimeException(messageSource.getMessage("user.required", null, Locale.getDefault()));

        long nbEmpruntsActifs = empruntRepository.countByUtilisateur_EmailAndDateDeRetourEffectiveIsNull(lecteur.getEmail());
        if (nbEmpruntsActifs >= LIMITE_MAX_EMPRUNTS) {
            throw new RuntimeException(messageSource.getMessage("loan.limit.reached", null, Locale.getDefault()));
        }

        if (emprunt.getLivre().getStock() <= 0) {
            throw new RuntimeException(messageSource.getMessage("loan.not.available", null, Locale.getDefault()));
        }
    }

    @Transactional
    @Override
    public Emprunt emprunterLivre(Emprunt emprunt) {
        Livre livreBdd = livreRepository.findById(emprunt.getLivre().getId())
                .orElseThrow(() -> new RuntimeException("Livre introuvable"));

        emprunt.setLivre(livreBdd);

        validerEmprunt(emprunt);

        emprunt.setStatut(StatutEmprunt.EMPRUNTE);
        emprunt.setDateDEmprunt(LocalDateTime.now());
        emprunt.setDateDeRetourAttendue(LocalDateTime.now().plusDays(DUREE_EMPRUNT_JOURS));

        livreBdd.setStock(livreBdd.getStock() - 1);

        return empruntRepository.save(emprunt);
    }

    @Transactional
    @Override
    public void validerRetourLivre(Emprunt emprunt) {
        Emprunt empruntEnBase = empruntRepository.findById(emprunt.getId())
                .orElseThrow(() -> new RuntimeException(messageSource.getMessage("loan.not.found", null, Locale.getDefault())));
        empruntEnBase.setStatut(StatutEmprunt.RENDU);
        empruntEnBase.setDateDeRetourEffective(LocalDateTime.now());
        Livre livre = empruntEnBase.getLivre();
        livre.setStock(livre.getStock() + 1);

        empruntRepository.save(empruntEnBase);
    }

    @Override
    public List<Emprunt> afficherHistoriqueDEmprunts(String emailLecteur) {
        return empruntRepository.findByUtilisateur_EmailOrderByDateDEmpruntDesc(emailLecteur);
    }
}
