package com.example.farmmate.Services.GProduit;

import com.example.farmmate.Entities.GProduit.Panier;
import com.example.farmmate.Entities.GProduit.Produit;
import com.example.farmmate.Entities.GProduit.StatusProduit;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IproduitService {
    Produit addProduit(Produit produit ,Long idUser);
    Produit updateProduit(Produit produit , Long idProduit );
    Produit retriveProduit(Long idProduit );
    void deletePiste(Long idProduit);
    List<Produit> retriveAll();
    public List<Produit> ListProduitByUser(Long idUser) ;
    public List<Produit> ListProduitByStatusEnAttente() ;
    public Produit updateStatusAndRemarque(Long idProduit, StatusProduit statusProduit, String remarque) ;
    double calculerChiffreAffaires(Long idUser);
    double calculerCoutTotal(Long idUser);
    double calculerBenefice(Long idUser);
    double calculerMargeBeneficiaire(Long idUser);
    double calculerValeurStock(Long idUser);
    List<Produit> getProduitsLesPlusVendus(Long idUser, int limit);
    Map<String, Map<String, Double>> getStatsParCategorie(Long idUser);
    List<Produit> getProduitsFaibleStock(Long idUser, int pourcentage);
    Map<String, Object> getStatistiquesResume(Long idUser);
    public void activerVenteFlash();
    public void desactiverVenteFlash();
    public List<Produit> getProduitsVenteFlash();
    public boolean isVenteFlashActive();
    public LocalDate convertToLocalDate(Date dateToConvert);
    }
