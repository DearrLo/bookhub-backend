package com.bookhub.bo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" })
@Entity
@Table(name = "BOOK")
public class Livre {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BOOK_ID")
	private Integer id;

	@Column(name = "ISBN", nullable = false, length = 20, unique = true)
	private String isbn;

	@Column(name = "TITLE", nullable = false, length = 255) 
	private String titre;

	@Column(name = "AUTHOR", nullable = false, length = 255)
	private String auteur;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_ID", nullable = false)
	private Categorie categorie;

	@Column(name = "STOCK", nullable = false)
	private Integer stock;

	@Column(name = "SUMMARY")
	private String resume;

	@Column(name = "COVER_IMAGE_URL")
	private String coverImageUrl;

	@Column(name = "CREATED_AT")
	private LocalDateTime createdAt;
}
