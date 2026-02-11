package com.bookhub.dal;

import com.bookhub.bo.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface de persistance pour les catégories littéraires.
 */
public interface CategorieRepository extends JpaRepository<Categorie, Integer> {
}