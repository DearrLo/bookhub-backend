package com.bookhub.bo;

import jakarta.persistence.*;
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

    @Column(name = "COMMENTARY", nullable = false, length = 500)
    private String commentaire;

    @Column(name = "RATING", nullable = false)
    private int note;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime dateDeCreation;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private Livre livre;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private Utilisateur utilisateur;
}
