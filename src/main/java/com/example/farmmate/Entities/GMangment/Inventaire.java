package com.example.farmmate.Entities.GMangment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "inventaires")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Inventaire implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idInventaire;

    private String nomArticle;
    private String description;
    private String categorie;
    private int quantiteEnStock;
    private String uniteDeMesure;
    private float prixUnitaire;
    private String fournisseurEmail;
    private int niveauDeReapprovisionnement;
    private int quantiteDeReapprovisionnement;

    // Using LocalDate for date fields; adjust if you prefer String
    private LocalDate dateDernierReapprovisionnement;
    private LocalDate dateExpiration;

    private String statutDeLInventaire;
    private LocalDate derniereMiseAJour;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;
}
