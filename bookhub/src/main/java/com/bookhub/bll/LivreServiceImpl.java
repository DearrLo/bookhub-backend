package com.bookhub.bll;

import com.bookhub.bo.Livre;
import com.bookhub.dal.LivreRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@AllArgsConstructor
@Service
public class LivreServiceImpl implements LivreService {

	private final LivreRepository livreRepository;
	private final MessageSource messageSource;

	@Override
	public List<Livre> afficherLivres() {
		return livreRepository.findAll();
	}

	@Override
	public Livre afficherDetailLivre(Integer id) {
		return livreRepository.findById(id)
				.orElse(null);
	}

	public void validerLivre(Livre livre){
		if (livre == null) {
			throw new RuntimeException(messageSource.getMessage("book.required", null, Locale.getDefault()));
		}
		if (livre.getIsbn() == null) {
			throw new RuntimeException(messageSource.getMessage("book.isbn.required", null, Locale.getDefault()));
		}
		if (livreRepository.existsByIsbn(livre.getIsbn())) {
			throw new RuntimeException(messageSource.getMessage("book.isbn.exists", new Object[]{livre.getIsbn()}, Locale.getDefault()));
		}
		if (livre.getCategorie() == null) {
			throw new RuntimeException(messageSource.getMessage("category.required", null, Locale.getDefault()));
		}
		if (livre.getAuteur() == null) {
			throw new RuntimeException(messageSource.getMessage("book.author.required", null, Locale.getDefault()));
		}
		if (livre.getTitre() == null) {
			throw new RuntimeException(messageSource.getMessage("book.title.required", null, Locale.getDefault()));
		}
		if (livre.getStock() == null) {
			throw new RuntimeException(messageSource.getMessage("book.stock.required", null, Locale.getDefault()));
		}
	}

	@Override
	public Livre ajouter(Livre livre) {
		validerLivre(livre);
		livre.setDateDeCreation(LocalDateTime.now());
		try {
			return livreRepository.save(livre);
		} catch (RuntimeException e) {
			throw new RuntimeException(messageSource.getMessage("book.save.failed", null, Locale.getDefault()));
		}
	}

	@Override
	public Livre modifier(Livre livreEnvoye) {
		Livre livreEnBase = livreRepository.findById(livreEnvoye.getId())
				.orElseThrow(() -> new RuntimeException(messageSource.getMessage("book.not.found", null, Locale.getDefault())));

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
		return livreRepository.findByStockGreaterThan(0);
	}
}