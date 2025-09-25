package com.example.farmmate.Entities.GMangment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "champs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
 class Champ implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer champId;

    private String plantesType;
    private int plantesNum;
    private float area;
    private String irrigationType;
    private float production;
    private LocalDate plantingDate;
    private LocalDate harvestDate;
    private float productivityScore;
    private float sustainabilityIndex;
    private float waterStressIndex;
    private String pestHistory;
    private String historicalYields;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;
}
