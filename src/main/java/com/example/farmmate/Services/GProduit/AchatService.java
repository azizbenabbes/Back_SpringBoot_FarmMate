package com.example.farmmate.Services.GProduit;

import com.example.farmmate.Entities.GProduit.HistoriqueAchat;
import com.example.farmmate.Entities.GProduit.HistoriqueProduit;
import com.example.farmmate.Entities.GProduit.Panier;
import com.example.farmmate.Entities.GProduit.PanierProduit;
import com.example.farmmate.Entities.Guser.User;
import com.example.farmmate.Repositories.GProduit.HistoriqueAchatRep;
import com.example.farmmate.Repositories.GProduit.HistoriqueProduitRep;
import com.example.farmmate.Repositories.GProduit.PanierProduitRep;
import com.example.farmmate.Repositories.GProduit.PanierRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service

public class AchatService {
    @Autowired
    private HistoriqueAchatRep historiqueAchatRep;

    @Autowired
    private HistoriqueProduitRep historiqueProduitRep;
    @Autowired
    private PanierProduitRep panierProduitRep;
    @Autowired
    private PanierRep panierRep;
    public void enregistrerAchat(User client, Set<PanierProduit> panierProduits, long montantTotal) {
        // Créer un nouvel historique d'achat
        HistoriqueAchat historiqueAchat = new HistoriqueAchat();
        historiqueAchat.setUserId(client.getIdUser());
        historiqueAchat.setDateAchat(new java.sql.Date(System.currentTimeMillis()));
        historiqueAchat.setMontantTotal(montantTotal);

        // Créer les entrées d'historique produit
        Set<HistoriqueProduit> historiqueProduits = new HashSet<>();
        for (PanierProduit panierProduit : panierProduits) {
            HistoriqueProduit historiqueProduit = new HistoriqueProduit();
            historiqueProduit.setProduit(panierProduit.getProduit());
            historiqueProduit.setQuantite(panierProduit.getQuantite());
            historiqueProduit.setPrixUnitaire(panierProduit.getProduit().getPrixProduit());
            historiqueProduit.setHistoriqueAchat(historiqueAchat);
            historiqueProduits.add(historiqueProduit);
        }

        // Sauvegarder l'historique d'achat
        historiqueAchat.setHistoriqueProduits(historiqueProduits);
        historiqueAchatRep.save(historiqueAchat);
        Panier panier = panierRep.findByUserId(client.getIdUser());

        if (panier != null) {
            panierRep.delete(panier);

        }
  }
    // Nouvelle méthode pour récupérer l'historique des achats d'un utilisateur
    public List<HistoriqueAchat> getHistoriqueAchatsByUser(Long userId) {
        return historiqueAchatRep.findByUserIdOrderByDateAchatDesc(userId);
    }

    // Méthode pour récupérer les détails d'un achat spécifique
    public HistoriqueAchat getHistoriqueAchatById(Long idHistorique) {
        return historiqueAchatRep.findById(idHistorique).orElse(null);
    }
}
