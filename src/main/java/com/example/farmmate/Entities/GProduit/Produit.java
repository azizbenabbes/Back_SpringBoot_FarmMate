package com.example.farmmate.Entities.GProduit;
import com.example.farmmate.Entities.Guser.Agriculteur;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor // constr par default
@AllArgsConstructor // constructeur par
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE) // mathouthech privee
@Entity
public class Produit {
    @Id // cle prim
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduit ;
    private String nomProduit;
    private int stockProduit ;
    private  int prixProduit;
    private Date dateFa ;
    private Date dateExp ;
    private String imageProduit ;
    @Enumerated(EnumType.STRING)
    private StatusProduit statusProduit;
    @Enumerated(EnumType.STRING)
    private CategorieProduit categorieProduit;
    private String description ;
    private String remarque  ;
    private int countt  ;
    private int prixReel  ;
    private boolean enVenteFlash = false;
    @JsonIgnore
    @OneToMany(mappedBy = "produit" )
    Set<PanierProduit> panierProduits;
     @ManyToOne
     Agriculteur agriculteur ;


}
