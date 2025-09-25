package com.example.farmmate.Entities.GMangment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "equipements")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Equipement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer equipID;

    private String statut;
    private float scoreEfficacite;
    private float tauxCouvertureSurface;
    private float coutHoraire;
    private float coutParKm;

    private LocalDate prochaineDateEntretien;
    private LocalDate dateDernierEntretien;

    private float totalHeuresFonctionnement;
    private String historiqueEntretien;
    private String categorieActif;
    private float valeurRevente;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;
}
