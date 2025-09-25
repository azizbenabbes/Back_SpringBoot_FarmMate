package com.example.farmmate.Entities.GSerre;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "materiel")
public class Materiel implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String modele;

    @Column(nullable = false)
    private String statut;

    private String emplacement;

    @Column(name = "date_installation", nullable = false)
    private LocalDateTime dateInstallation;

    @ManyToOne
    @JoinColumn(name = "serre_id", nullable = false)
    private Serre serre;

}
