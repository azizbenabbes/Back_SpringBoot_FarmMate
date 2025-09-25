package com.example.farmmate.Entities.GProduit;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Reclamation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReclamation;
    @Column(columnDefinition = "TEXT") // Placez cette annotation ici

    private String contenuReclamation;
    @Column(columnDefinition = "TEXT")
    private String reponseReclamation;
    private Date dateReponse; // Date de la réponse

    private Date dateReclamation;

    private String statusReclamation; // "NOUVELLE", "EN_TRAITEMENT", "RÉSOLUE"
    private Boolean iaGeneree; // Indique si la réclamation a été générée par l'IA

    @ManyToOne
    @JoinColumn(name = "id_historique")
    @JsonBackReference
    private HistoriqueAchat historiqueAchat;

    @ManyToOne
    @JoinColumn(name = "id_produit")
    private Produit produit;

    private Long userId; // ID de l'utilisateur qui fait la réclamation
}
