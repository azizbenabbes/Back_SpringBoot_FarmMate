package com.example.farmmate.Services.GProduit;

import com.example.farmmate.Entities.GProduit.HistoriqueAchat;
import com.example.farmmate.Entities.GProduit.PanierProduit;
import com.example.farmmate.Entities.Guser.User;

import java.util.List;
import java.util.Set;

public interface IachatService {
    public void enregistrerAchat(User client, Set<PanierProduit> panierProduits, long montantTotal);
    public List<HistoriqueAchat> getHistoriqueAchatsByUser(Long userId) ;


    // Méthode pour récupérer les détails d'un achat spécifique
    public HistoriqueAchat getHistoriqueAchatById(Long idHistorique) ;

}
