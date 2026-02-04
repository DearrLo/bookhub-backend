package com.bookhub.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of= {"id"})

@Entity
@Table(name = "CATEGORY")
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CATEGORY_ID")
    private Integer id;

    @NotBlank
    @Column(name = "LABEL", nullable = false, length = 20)
    private String libelle;

}
