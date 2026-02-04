package com.bookhub.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of= {"id"})

@Entity
@Table(name = "REVIEW")
public class Commentaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="REVIEW_ID")
    private Integer id;

    @NotBlank
    @Column(name = "COMMENTARY", nullable = false, length = 500)
    private String commentaire;

    @NotNull
    @Min(value = 0, message = "La note minimale est 0")
    @Max(value = 5, message = "La note maximale est 5")
    @Column(name = "RATING", nullable = false)
    private Integer note;

    @NotNull
    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime dateDeCreation;

//    @ManyToOne
//    @JoinColumn(name = "BOOK_ID", nullable = false)
//    private Livre livre;
//
//    @ManyToOne
//    @JoinColumn(name = "USER_ID", nullable = false)
//    private Utilisateur utilisateur;
}
