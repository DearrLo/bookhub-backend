package com.bookhub.bll;

import java.time.LocalDateTime;
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

	public void validerLivre(Livre livre){
		if (livre == null) {
			throw new RuntimeException("Le livre est obligatoire");
		}
		if (livre.getIsbn() == null) {
			throw new RuntimeException("L'ISBN est obligatoire");
		}
		if (livreRepository.existsByIsbn(livre.getIsbn())) {
			throw new RuntimeException("Un livre avec l'ISBN " + livre.getIsbn() + " existe déjà.");
		}
		if (livre.getCategorie() == null) {
			throw new RuntimeException("La categorie est obligatoire");
		}
		if (livre.getAuteur() == null) {
			throw new RuntimeException("L'auteur est obligatoire");
		}
		if (livre.getTitre() == null) {
			throw new RuntimeException("Le titre est obligatoire");
		}
		if (livre.getStock() == null) {
			throw new RuntimeException("Le stock est obligatoire");
		}
	}

	@Override
	public Livre ajouter(Livre livre) {
		validerLivre(livre);
		livre.setDateDeCreation(LocalDateTime.now());
        try {
            return livreRepository.save(livre);
        } catch (RuntimeException e) {
            throw new RuntimeException("Impossible de sauver - " + livre.toString());
        }
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
