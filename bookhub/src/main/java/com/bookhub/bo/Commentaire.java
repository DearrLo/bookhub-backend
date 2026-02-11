package com.bookhub.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Représente un avis et une note laissés par un lecteur sur un ouvrage.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of= {"id"})
@Entity
@Table(name = "REVIEW")
public class Commentaire {

    /** Identifiant unique du commentaire. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="REVIEW_ID")
    private Integer id;

    /** Texte de l'avis utilisateur. */
    @NotBlank
    @Column(name = "COMMENTARY", nullable = false, length = 500)
    private String commentaire;

    /** Note attribuée sur 5. */
    @NotNull
    @Min(value = 0, message = "La note minimale est 0")
    @Max(value = 5, message = "La note maximale est 5")
    @Column(name = "RATING", nullable = false)
    private Integer note;

    /** Date de publication de l'avis. */
    @NotNull
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime dateDeCreation;

    /** Livre associé à l'avis. */
    @ManyToOne
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private Livre livre;

    /** Auteur du commentaire. */
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private Utilisateur utilisateur;
}