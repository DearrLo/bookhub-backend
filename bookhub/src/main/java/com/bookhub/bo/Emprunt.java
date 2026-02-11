package com.bookhub.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entité gérant le cycle de vie d'un prêt de livre.
 * Lie un ouvrage à un lecteur avec un suivi des échéances de retour.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of= {"id"})
@Entity
@Table(name = "BOOKHUB_LOAN")
public class Emprunt {

    /** Identifiant unique de la transaction d'emprunt. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="LOAN_ID")
    private Integer id;

    /** Date à laquelle le livre a été emprunté. */
    @NotNull
    @Column(name = "LOAN_DATE", nullable = false)
    private LocalDateTime dateDEmprunt;

    /** Date limite théorique pour le retour du livre. */
    @NotNull
    @Column(name = "DATE_TO_RETURN", nullable = false)
    private LocalDateTime dateDeRetourAttendue;

    /** Date réelle à laquelle le livre a été rendu. */
    @Column(name = "RETURNED_DATE")
    private LocalDateTime dateDeRetourEffective;

    /** Livre concerné par l'emprunt. */
    @ManyToOne
    @NotNull
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private Livre livre;

    /** Utilisateur ayant effectué l'emprunt. */
    @ManyToOne
    @NotNull
    @JoinColumn(name = "USER_ID", nullable = false)
    private Utilisateur utilisateur;

    /** État actuel de l'emprunt (EMPRUNTE ou RENDU). */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "LOAN_STATUS", length = 20, nullable = false)
    private StatutEmprunt statut;
}