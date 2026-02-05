package com.bookhub.bll;

import com.bookhub.bo.Utilisateur;

public interface UtilisateurService {

    void ajouterUtilisateur(Utilisateur utilisateur);

    Utilisateur modifierUtilisateur(Utilisateur utilisateur);

    void supprimerUtilisateur(String email);
}


