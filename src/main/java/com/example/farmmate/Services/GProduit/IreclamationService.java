package com.example.farmmate.Services.GProduit;

import com.example.farmmate.Entities.GProduit.Reclamation;

import java.util.List;

public interface IreclamationService {
    public Reclamation creerReclamation(Long idHistorique, Long idProduit, Long userId, String contenu, Boolean iaGeneree);
    public List<Reclamation> getReclamationsParUtilisateur(Long userId);
    public List<Reclamation> getReclamationsParAchat(Long idHistorique);
    public List<Reclamation> getReclamationsParAgriculteur(Long agriculteurId);
    public Reclamation updateStatutReclamation(Long idReclamation, String nouveauStatut);
    public String genererReclamationAvecIA(Long userId, String problemeDescription, String produitNom);
    public Reclamation ajouterReponseReclamation(Long idReclamation, String reponse) ;




}
