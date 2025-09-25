package com.example.farmmate.Entities.GRdv;


import com.example.farmmate.Entities.Guser.Specialist;
import com.example.farmmate.Entities.Guser.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Planning  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idPlanning;


    @OneToOne
    Specialist specialist;

    @OneToMany(mappedBy = "planning",cascade = CascadeType.REMOVE)
    Set<RendezVous> rendezVousSet;

    @OneToMany(mappedBy = "planning", cascade = CascadeType.ALL)
    Set<Disponibilite> disponibilites;
}
