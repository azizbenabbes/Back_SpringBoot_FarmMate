package com.example.farmmate.Entities.GJob;

import com.example.farmmate.Entities.GProduit.CategorieProduit;
import com.example.farmmate.Entities.Guser.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Annonce {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String description;
    private LocalDate datePublication;
    private double salaire;
    private String localisation;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private CategorieProduit categorieProduit; // Catégorie comme simple attribut


    @ManyToOne
    @JoinColumn(name = "paysan_id", nullable = false)
    private User paysan;


}
