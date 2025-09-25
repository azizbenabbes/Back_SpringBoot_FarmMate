package com.example.farmmate.Repositories.GProduit;

import com.example.farmmate.Entities.GProduit.Reclamation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReclamationRep extends JpaRepository<Reclamation, Long> {
    List<Reclamation> findByUserIdOrderByDateReclamationDesc(Long userId);
    List<Reclamation> findByHistoriqueAchatIdHistorique(Long idHistorique);
    List<Reclamation> findByProduit_Agriculteur_IdUser(Long agriculteurId);


}
