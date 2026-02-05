package com.bookhub.bll;

import com.bookhub.bo.Emprunt;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EmpruntService {


    @Transactional
    Emprunt demandeEmpruntLivre(Emprunt emprunt);

    @Transactional
    void validerEmpruntLivre(Emprunt emprunt);

    void RendreLivre(Emprunt emprunt);

    @Transactional
    void validerRetourLivre(Emprunt emprunt);

    List<Emprunt> afficherHistoriqueDEmprunts(String emailLecteur);
}
