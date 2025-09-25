package com.example.farmmate.Services.GProduit;

import com.example.farmmate.Entities.GProduit.Panier;
import com.example.farmmate.Entities.GProduit.Produit;

import java.util.List;
import java.util.Map;

public interface IpanierService {
    Panier addPanier(Long idUsersession,Long idproduit,int quantitePanier);
    Panier updatePanier(Panier panier);
    Panier retrivePanier(Long idPanier);
    void deletePanier(Long idPanier);
    List<Panier> retriveAll();
    public long  calculerTotalPanier(Long idPanier ) ;
    public Panier supprimerProduitDuPanier(Long idUsersession, Long idproduit);
    public List<Produit> getProduitsDuPanier(Long idUsersession);
    public Panier modifierQuantitePanier(Long idUsersession, Long idproduit, int nouvelleQuantite) ;
    public Map<String, Object> createPersonalizedCart(Long userId, int budget, String category);
    public List<Map<String, Object>> getProduitsDuPanierWithQuantity(Long idUsersession);
}
