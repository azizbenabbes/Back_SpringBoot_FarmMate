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
public class CriseAgricole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   long idCrise ;

    @Enumerated(EnumType.STRING)
   TypeCulture culture ; //enum
Float superficie;
String localisation ; //avec maps
String image; //s il veut poster une image de probleme

/*@ElementCollection
List<String> descrptionDeProblemeAvecImage;*/

Float temperature;
Float humidite;

Float estimationRisque; //pour des previsions
    @OneToOne
    RendezVous rendezvous;

    @OneToOne
    Rapport rapport;
}
