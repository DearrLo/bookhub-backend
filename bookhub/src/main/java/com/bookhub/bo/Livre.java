package com.bookhub.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Représente un ouvrage au sein du catalogue de la bibliothèque.
 * Cette entité gère les informations bibliographiques et l'état des stocks.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "isbn" })
@Entity
@Table(name = "BOOK")
public class Livre {

	/** Identifiant unique auto-généré du livre. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BOOK_ID")
	private Integer id;

	/** Code ISBN-13 unique servant d'identifiant métier international. */
	@NotNull
	@Column(name = "ISBN", nullable = false, length = 20, unique = true)
	private String isbn;

	/** Titre de l'ouvrage. */
	@NotNull
	@Column(name = "TITLE", nullable = false, length = 255)
	private String titre;

	/** Auteur du livre. */
	@NotNull
	@Column(name = "AUTHOR", nullable = false, length = 255)
	private String auteur;

	/** Catégorie littéraire associée (Relation Many-to-One). */
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_ID", nullable = false)
	private Categorie categorie;

	/** Quantité d'exemplaires physiques disponibles pour le prêt. */
	@NotNull
	@Column(name = "STOCK", nullable = false)
	private Integer stock;

	/** Résumé ou quatrième de couverture du livre. */
	@Column(name = "SUMMARY")
	private String resume;

	/** URL de l'image de couverture pour l'affichage Front-end. */
	@Column(name = "COVER_IMAGE_URL")
	private String urlImage;

	/** Date et heure de l'ajout du livre dans la base. */
	@Column(name = "CREATED_AT")
	private LocalDateTime dateDeCreation;

	/** Liste des avis et notes laissés par les utilisateurs sur ce livre. */
	@OneToMany(mappedBy = "livre", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties("livre")
	@ToString.Exclude
	private List<Commentaire> commentaires;
}