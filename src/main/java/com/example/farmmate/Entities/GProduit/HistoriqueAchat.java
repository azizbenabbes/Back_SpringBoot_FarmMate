package com.example.farmmate.Entities.GProduit;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class HistoriqueAchat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistorique;

    private Date dateAchat;
    private long montantTotal;
    private Long userId; // Id du client (user) qui effectue l'achat

    @OneToMany(mappedBy = "historiqueAchat", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<HistoriqueProduit> historiqueProduits;
}
