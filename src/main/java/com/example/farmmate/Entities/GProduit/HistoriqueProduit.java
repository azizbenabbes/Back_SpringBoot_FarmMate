package com.example.farmmate.Entities.GProduit;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class HistoriqueProduit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_historique")
    @JsonBackReference

    private HistoriqueAchat historiqueAchat;

    @ManyToOne
    @JoinColumn(name = "id_produit")
    private Produit produit;

    private int quantite;
    private int prixUnitaire;  // Le prix du produit au moment de l'achat
}
