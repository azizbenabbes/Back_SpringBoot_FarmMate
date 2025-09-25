package com.example.farmmate.Repositories.GProduit;

import com.example.farmmate.Entities.GProduit.HistoriqueAchat;
import com.example.farmmate.Entities.GProduit.HistoriqueProduit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriqueAchatRep  extends JpaRepository<HistoriqueAchat, Long> {
    List<HistoriqueAchat> findByUserIdOrderByDateAchatDesc(Long userId);

}
