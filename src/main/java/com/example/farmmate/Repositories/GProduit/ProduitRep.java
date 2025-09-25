package com.example.farmmate.Repositories.GProduit;

import com.example.farmmate.Entities.GProduit.Panier;
import com.example.farmmate.Entities.GProduit.Produit;
import com.example.farmmate.Entities.GProduit.StatusProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ProduitRep extends JpaRepository<Produit,Long> {
    List<Produit> findAllByStatusProduit(StatusProduit statusProduit);
    List<Produit> findAllByAgriculteur_IdUser(Long idUser);
    List<Produit> findAllByEnVenteFlashTrue();

}
