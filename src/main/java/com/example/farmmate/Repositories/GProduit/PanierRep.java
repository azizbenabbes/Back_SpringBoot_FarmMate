package com.example.farmmate.Repositories.GProduit;

import com.example.farmmate.Entities.GProduit.Panier;
import com.example.farmmate.Entities.GProduit.PanierProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PanierRep extends JpaRepository<Panier,Long> {
    Panier findByUserId(Long userId);

}
