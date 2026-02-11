package com.bookhub.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Représente une demande de réservation lorsqu'un livre n'est plus en stock.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of= {"id"})
@Entity
@Table(name = "RESERVATION")
public class Reservation {

    /** Identifiant unique de la réservation. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="RESERVATION_ID")
    private Integer id;

    /** Date et heure de la demande de réservation. */
    @NotNull
    @Column(name = "REQUEST_DATE", nullable = false)
    private LocalDateTime dateDeDemande;

    /** Statut de la réservation (EN_ATTENTE, DISPO, ANNULEE). */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "RESERVATION_STATUS", length = 20, nullable = false)
    private StatutResa statut;

    /** Livre réservé. */
    @ManyToOne
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private Livre livre;

    /** Utilisateur ayant fait la demande. */
    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private Utilisateur utilisateur;
}