package com.bookhub.bll;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bookhub.bo.Livre;
import com.bookhub.dal.LivreRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class LivreServiceImpl implements LivreService {

	private final LivreRepository livreRepository;

	@Override
	public List<Livre> afficherLivres() {
		return livreRepository.findAll();
	}

	@Override
	public Livre afficherDetailLivre(Integer id) {
		return livreRepository	.findById(id)
								.orElse(null);
	}

	@Override
	public Livre ajouter(Livre livre) {
		return livreRepository.save(livre);
	}

	@Override
	public Livre modifier(Livre livreEnvoye) {
		Livre livreEnBase = livreRepository.findById(livreEnvoye.getId())
				.orElseThrow(() -> new RuntimeException("Livre introuvable"));

		if (livreEnvoye.getTitre() != null) {
			livreEnBase.setTitre(livreEnvoye.getTitre());
		}

		if (livreEnvoye.getAuteur() != null) {
			livreEnBase.setAuteur(livreEnvoye.getAuteur());
		}

		if (livreEnvoye.getIsbn() != null) {
			livreEnBase.setIsbn(livreEnvoye.getIsbn());
		}

		if (livreEnvoye.getStock() != null) {
			livreEnBase.setStock(livreEnvoye.getStock());
		}

		if (livreEnvoye.getCategorie() != null) {
			livreEnBase.setCategorie(livreEnvoye.getCategorie());
		}

		return livreRepository.save(livreEnBase);
	}

	@Override
	public void supprimer(Integer id) {
		livreRepository.deleteById(id);
	}

	@Override
	public List<Livre> chargerLivreParCategorie(Integer idCategorie) {
		return livreRepository.findByCategorie_Id(idCategorie);
	}

	@Override
	public List<Livre> rechercherLivres(String critere) {
		return livreRepository.findByTitreContainingIgnoreCaseOrAuteurContainingIgnoreCaseOrIsbnContainingIgnoreCase(
				critere, critere, critere
		);
	}

	@Override
	public List<Livre> chargerLivreParDisponibilite() {

		// Un livre est disponible si stock > 0
		return livreRepository.findByStockGreaterThan(0);
	}
}
