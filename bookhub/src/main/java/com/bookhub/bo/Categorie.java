package com.bookhub.bo;

import jakarta.persistence.*;
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

    @Column(name = "LABEL", nullable = false, length = 20)
    private String libelle;

}
