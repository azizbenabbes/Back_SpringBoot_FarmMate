package com.example.farmmate.Entities.GRdv;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Rapport  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long IdRapport  ;

String descrptionRapport ;

@OneToOne(mappedBy = "rapport")
    CriseAgricole criseagricole;
    long specId;


}
