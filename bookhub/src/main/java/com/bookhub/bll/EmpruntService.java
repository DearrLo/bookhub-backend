package com.bookhub.bll;

import com.bookhub.bo.Emprunt;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmpruntService {


    List<Emprunt> affficherEmpruntsActifs();

    @Transactional
    Emprunt emprunterLivre(Emprunt emprunt);

    @Transactional
    void validerRetourLivre(Emprunt emprunt);

    List<Emprunt> afficherHistoriqueDEmprunts(String emailLecteur);
}
