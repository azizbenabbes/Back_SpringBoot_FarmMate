package com.example.farmmate.Entities.GProduit;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor // constr par default
@AllArgsConstructor // constructeur par
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE) // mathouthech privee
@Entity
public class Panier {
    @Id // cle prim
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPanier ;
    private Date dateCreation ;

    // Relation many-to-many via table de jointure PanierProduit
    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<PanierProduit> panierProduits = new HashSet<>();
    long userId;


}
