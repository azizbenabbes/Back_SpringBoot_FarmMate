package com.example.farmmate.Entities.GRdv;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Disponibilite  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idDisponibilite;

    @Enumerated(EnumType.STRING)
    JourSemaine jour;

    LocalTime heureDebut;
    LocalTime heureFin;

    @ManyToOne
    Planning planning;
}
