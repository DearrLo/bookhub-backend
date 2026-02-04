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
@Table(name = "BORROWING")
public class Emprunt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="BORROWING_ID")
    private Integer id;

    @Column(name = "BORROWING_DATE", nullable = false)
    private LocalDateTime dateDEmprunt;

    @Column(name = "DATE_TO_RETURN", nullable = false)
    private LocalDateTime dateDeRetourAttendue;

    @Column(name = "RETURNED_DATE")
    private LocalDateTime dateDeRetourEffective;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private Livre livre;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private Utilisateur utilisateur;

}
