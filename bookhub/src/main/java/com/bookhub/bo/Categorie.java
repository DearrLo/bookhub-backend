package com.bookhub.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Représente une catégorie littéraire (ex: Roman, Science-Fiction).
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of= {"id"})
@Entity
@Table(name = "CATEGORY")
public class Categorie {

    /** Identifiant unique de la catégorie. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CATEGORY_ID")
    private Integer id;

    /** Nom ou libellé de la catégorie. */
    @NotBlank
    @Size(max = 20)
    @Column(name = "LABEL", nullable = false, length = 20)
    private String libelle;
}