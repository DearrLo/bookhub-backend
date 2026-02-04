package com.bookhub.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of= {"id"})

@Entity
@Table(name = "BORROWING")
public class Emprunt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="BORROWING_ID")
    private Integer id;

    @NotNull
    @Column(name = "BORROWING_DATE", nullable = false)
    private LocalDateTime dateDEmprunt;

    @NotNull
    @Column(name = "DATE_TO_RETURN", nullable = false)
    private LocalDateTime dateDeRetourAttendue;

    @Column(name = "RETURNED_DATE")
    private LocalDateTime dateDeRetourEffective;

//    @ManyToOne
//    @NotNull
//    @JoinColumn(name = "BOOK_ID", nullable = false)
//    private Livre livre;
//
//    @ManyToOne
//    @NotNull
//    @JoinColumn(name = "USER_ID", nullable = false)
//    private Utilisateur utilisateur;

}
