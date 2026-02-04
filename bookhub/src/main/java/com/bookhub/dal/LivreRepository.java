package com.bookhub.dal;

import com.bookhub.bo.Livre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivreRepository extends JpaRepository<Livre, Integer> {

    List<Livre> findByTitreContainingIgnoreCase(String titre);
    List<Livre> findByAuteurContainingIgnoreCase(String auteur);
    List<Livre> findByIsbnContainingIgnoreCase(String isbn);

    List<Livre> findByTitreContainingIgnoreCaseOrAuteurContainingIgnoreCaseOrIsbnContainingIgnoreCase(
            String titre,
            String auteur,
            String isbn
    );

    List<Livre> findByCategorie_Id(Integer categorieId);
    List<Livre> findByStockGreaterThan(Integer stock);
}
