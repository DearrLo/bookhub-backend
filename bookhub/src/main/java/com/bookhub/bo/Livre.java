package com.bookhub.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "isbn" })
@Entity
@Table(name = "BOOK")
public class Livre {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BOOK_ID")
	private Integer id;

	@NotNull
	@Column(name = "ISBN", nullable = false, length = 20, unique = true)
	private String isbn;

	@NotNull
	@Column(name = "TITLE", nullable = false, length = 255) 
	private String titre;

	@NotNull
	@Column(name = "AUTHOR", nullable = false, length = 255)
	private String auteur;

	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_ID", nullable = false)
	private Categorie categorie;

	@NotNull
	@Column(name = "STOCK", nullable = false)
	private Integer stock;

	@Column(name = "SUMMARY")
	private String resume;

	@Column(name = "COVER_IMAGE_URL")
	private String urlImage;

	@Column(name = "CREATED_AT")
	private LocalDateTime dateDeCreation;

	@OneToMany(mappedBy = "livre", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnoreProperties("livre")
	@ToString.Exclude
	private List<Commentaire> commentaires;
}
