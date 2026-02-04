package com.bookhub.dal;

import com.bookhub.bo.Categorie;
import com.bookhub.bo.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<Categorie, Integer> {
}
