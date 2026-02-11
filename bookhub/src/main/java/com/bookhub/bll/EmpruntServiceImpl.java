package com.bookhub.bll;

import com.bookhub.bo.*;
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

    /**
     * Récupère la liste de tous les emprunts actifs (non rendus).
     * @return Liste des emprunts dont le statut n'est pas 'RENDU'.
     */
    @Override
    public List<Emprunt> affficherEmpruntsActifs() {
        return empruntRepository.findByStatutNot(StatutEmprunt.RENDU);
    }

    /**
     * Identifie les emprunts non rendus dont la date d'échéance est dépassée.
     * @param utilisateur L'utilisateur concerné par la vérification.
     * @return Liste des emprunts en retard.
     */
    private List<Emprunt> recupererListeEmpruntsEnRetard(Utilisateur utilisateur){ return empruntRepository.findByUtilisateurAndDateDeRetourEffectiveIsNullAndDateDeRetourAttendueBefore(utilisateur, LocalDateTime.now());};

    /**
     * Vérifie si un emprunt est possible selon les règles métier.
     * Contrôle : existence du livre/utilisateur, absence de retards, limite de 3 emprunts, stock disponible.
     * @param emprunt L'objet emprunt à valider.
     * @throws RuntimeException si une condition de prêt n'est pas remplie.
     */
    private void validerEmprunt(Emprunt emprunt) {
        if (emprunt == null) throw new RuntimeException(messageSource.getMessage("loan.required", null, Locale.getDefault()));
        if (emprunt.getLivre() == null) throw new RuntimeException(messageSource.getMessage("book.required", null, Locale.getDefault()));

        Utilisateur lecteur = emprunt.getUtilisateur();
        if (lecteur == null) throw new RuntimeException(messageSource.getMessage("user.required", null, Locale.getDefault()));

        if (!recupererListeEmpruntsEnRetard(lecteur).isEmpty()){
            throw new RuntimeException(messageSource.getMessage("loan.impossible.late", null, Locale.getDefault()));
        }

        long nbEmpruntsActifs = empruntRepository.countByUtilisateur_EmailAndDateDeRetourEffectiveIsNull(lecteur.getEmail());
        if (nbEmpruntsActifs >= LIMITE_MAX_EMPRUNTS) {
            throw new RuntimeException(messageSource.getMessage("loan.limit.reached", null, Locale.getDefault()));
        }
        if (emprunt.getLivre().getStock() <= 0) {
            throw new RuntimeException(messageSource.getMessage("loan.not.available", null, Locale.getDefault()));
        }

    }

    /**
     * Enregistre un nouvel emprunt en base de données.
     * Applique les règles de gestion : vérifie si le livre est en stock,
     * si l'utilisateur n'a pas dépassé la limite d'emprunts et s'il n'est pas en retard.
     * Décrémente automatiquement le stock du livre concerné.
     * * @param emprunt L'objet emprunt contenant le livre et l'utilisateur.
     * @return L'emprunt sauvegardé avec les dates de retour calculées.
     * @throws RuntimeException si une règle de gestion n'est pas respectée.
     */
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

    /**
     * Valide le retour d'un livre.
     * Change le statut en "RENDU", enregistre la date réelle et incrémente le stock.
     * @param emprunt L'emprunt concerné.
     */
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

    /**
     * Récupère l'historique complet des emprunts d'un lecteur.
     * @param emailLecteur Identifiant de l'utilisateur.
     * @return Liste des emprunts triés du plus récent au plus ancien.
     */
    @Override
    public List<Emprunt> afficherHistoriqueDEmprunts(String emailLecteur) {
        return empruntRepository.findByUtilisateur_EmailOrderByDateDEmpruntDesc(emailLecteur);
    }
}
