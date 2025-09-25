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
public class CandidatureJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate datePostulation;

    @Enumerated(EnumType.STRING)
    private StatutCandidature statut;

    @ManyToOne
    @JoinColumn(name = "annonce_id", nullable = false)
    private Annonce annonce;

    @ManyToOne
    @JoinColumn(name = "employeur_id", nullable = false)
    private User employeur;

    }
