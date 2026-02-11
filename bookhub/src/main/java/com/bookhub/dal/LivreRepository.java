package com.bookhub.dal;

import com.bookhub.bo.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Interface de persistance pour l'entité Livre.
 * Gère l'accès aux données du catalogue, incluant les recherches multicritères et les filtres de stock.
 */
public interface LivreRepository extends JpaRepository<Livre, Integer> {

    /** Recherche les livres par titre (insensible à la casse). */
    List<Livre> findByTitreContainingIgnoreCase(String titre);

    /** Recherche les livres par auteur (insensible à la casse). */
    List<Livre> findByAuteurContainingIgnoreCase(String auteur);

    /** Recherche les livres par code ISBN. */
    List<Livre> findByIsbnContainingIgnoreCase(String isbn);

    /** Vérifie si un ouvrage existe déjà en base via son ISBN. */
    boolean existsByIsbn(String isbn);

    /**
     * Recherche globale dans le catalogue sur le titre, l'auteur ou l'ISBN.
     * @param titre Terme pour le titre.
     * @param auteur Terme pour l'auteur.
     * @param isbn Terme pour l'ISBN.
     * @return Liste des ouvrages correspondants.
     */
    List<Livre> findByTitreContainingIgnoreCaseOrAuteurContainingIgnoreCaseOrIsbnContainingIgnoreCase(
            String titre,
            String auteur,
            String isbn
    );

    /** Récupère les livres appartenant à une catégorie spécifique. */
    List<Livre> findByCategorie_Id(Integer categorieId);

    /** Récupère les livres dont le stock est strictement supérieur à un seuil. */
    List<Livre> findByStockGreaterThan(Integer stock);
}