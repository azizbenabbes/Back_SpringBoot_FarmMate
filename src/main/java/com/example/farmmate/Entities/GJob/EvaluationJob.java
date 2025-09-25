package com.example.farmmate.Entities.GJob;


import com.example.farmmate.Entities.Guser.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int note; // 1 à 5 étoiles
    private String commentaire;

    @ManyToOne
    @JoinColumn(name = "paysan_id", nullable = false)
    private User paysan; // Celui qui donne la note

    @ManyToOne
    @JoinColumn(name = "employeur_id", nullable = false)
    private User employeur; // Celui qui est évalué

    @OneToOne
    @JoinColumn(name = "contrat_id", nullable = false)
    private ContratJob contratJob; // L'évaluation est liée à un contrat
}
