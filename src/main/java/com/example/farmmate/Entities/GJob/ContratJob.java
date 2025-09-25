package com.example.farmmate.Entities.GJob;

import com.example.farmmate.Entities.Guser.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContratJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateSignature;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private double salaire;
    private boolean accepte;

    @ManyToOne
    @JoinColumn(name = "employeur_id", nullable = false)
    private User employeur;

    @ManyToOne
    @JoinColumn(name = "paysan_id", nullable = false)
    private User paysan;

    @OneToOne
    @JoinColumn(name = "candidature_id", nullable = false)
    private CandidatureJob candidatureJob;
}
