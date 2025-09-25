package com.example.farmmate.Repositories.GProduit;

import com.example.farmmate.Entities.GProduit.PanierProduit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PanierProduitRep extends JpaRepository<PanierProduit,Long> {
    void deleteAllByPanier_IdPanier(Long idPanier);
    void findByPanierUserId( Long userId);
}
