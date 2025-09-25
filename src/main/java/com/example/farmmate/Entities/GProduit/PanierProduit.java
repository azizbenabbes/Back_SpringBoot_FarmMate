package com.example.farmmate.Entities.GProduit;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class PanierProduit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_panier")
    @JsonBackReference

    private Panier panier;
    @JsonBackReference

    @ManyToOne
    @JoinColumn(name = "id_produit")
    private Produit produit;

        private int quantite;  // Quantité du produit dans ce panier
}
