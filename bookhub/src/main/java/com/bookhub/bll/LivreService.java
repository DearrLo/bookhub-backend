package com.bookhub.bll;

import com.bookhub.bo.Livre;

import java.util.List;

public interface LivreService {
	List<Livre> afficherLivres(); // TODO à modif avec pagination
	
	Livre afficherDetailLivre(Integer id);
	Livre ajouter(Livre livre);
	Livre modifier(Livre livre);
	void supprimer(Integer id);
	
	List<Livre> chargerLivreParCategorie (Integer idCategorie);

    List<Livre> rechercherLivres(String critere);

    // Retourne livre dispo si stock > 0
	List<Livre> chargerLivreParDisponibilite();
}
