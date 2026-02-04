package com.bookhub.dal;

import com.bookhub.bo.Commentaire;
import com.bookhub.bo.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentaireRepository extends JpaRepository<Commentaire, Integer> {
}
