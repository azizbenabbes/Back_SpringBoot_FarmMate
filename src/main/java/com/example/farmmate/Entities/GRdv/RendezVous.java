package com.example.farmmate.Entities.GRdv;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RendezVous implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     long idRdv;
    long userId;

    LocalDate dateRdv;

    @Enumerated(EnumType.STRING)
    StatusRdv statusrdv;

    @ManyToOne
    Planning planning;

    @OneToOne(mappedBy = "rendezvous")
    CriseAgricole criseAgricole;




}
