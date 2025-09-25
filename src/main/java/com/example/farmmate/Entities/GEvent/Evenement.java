package com.example.farmmate.Entities.GEvent;

import com.example.farmmate.Entities.Guser.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Evenement  implements Serializable  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idEvent;
    String nom;
    Date dateDebEvent;
    LocalDate dateFinEvent;
    String localisation;
    float prix;
    String description;
    int capacite;
    @Enumerated(EnumType.STRING)
    private TypeEvent  categorie;

    @ManyToOne
    @JoinColumn(name = "createur_id")
    private User createur;

    @OneToMany()
    private Set<User> participants = new HashSet<>();

    @OneToMany(mappedBy = "evenement_id", cascade = CascadeType.ALL)
    private Set<Participation> participations = new HashSet<>();



}
