package com.bookhub.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of= {"id"})

@Entity
@Table(name = "RESERVATION")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="RESERVATION_ID")
    private Integer id;

    @NotNull
    @Column(name = "REQUEST_DATE", nullable = false)
    private LocalDateTime dateDeDemande;

    @NotNull
    @Size(max = 20)
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Statut statut;

//    @ManyToOne
//    @JoinColumn(name = "BOOK_ID", nullable = false)
//    private Livre livre;
//
//    @ManyToOne
//    @JoinColumn(name = "USER_ID", nullable = false)
//    private Utilisateur utilisateur;

}
