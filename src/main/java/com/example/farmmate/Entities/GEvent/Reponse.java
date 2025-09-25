package com.example.farmmate.Entities.GEvent;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long idReponse;
    String contenu;

    private Long userId; // On stocke seulement l'ID de l'utilisateur

    @ManyToOne
    @JoinColumn(name = "commentaire_id")
    private Commentaire commentaire;




}
