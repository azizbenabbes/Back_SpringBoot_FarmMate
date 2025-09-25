package com.example.farmmate.Entities.GSerre;

import java.io.Serializable;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "plante")
@Getter
@Setter
public class Plante implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlantType species; // Using PlantType enum

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GrowthStage growthStage; // Using GrowthStage enum

    private float temperatureRequirement;
    private float humidityRequirement;
    private LocalDateTime lastWatered;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HealthStatus healthStatus; // Using HealthStatus enum

    @ManyToOne
    @JoinColumn(name = "serre_id", nullable = false)
    private Serre serre;
}
