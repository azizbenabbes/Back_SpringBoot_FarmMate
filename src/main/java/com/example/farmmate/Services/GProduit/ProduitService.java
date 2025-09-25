package com.example.farmmate.Services.GProduit;

import com.example.farmmate.Entities.GProduit.Produit;
import com.example.farmmate.Entities.GProduit.StatusProduit;
import com.example.farmmate.Entities.Guser.Agriculteur;
import com.example.farmmate.Entities.Guser.User;
import com.example.farmmate.Repositories.GProduit.PanierRep;
import com.example.farmmate.Repositories.GProduit.ProduitRep;
import com.example.farmmate.Repositories.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@CrossOrigin("*")
public class ProduitService implements IproduitService {

    @Autowired

    ProduitRep produitrep ;
    @Autowired

    UserRep userRep ;
    @Autowired

    PanierRep panierRep;
    private boolean venteFlashActive = false;

    private List<Produit> produitsVenteFlash;


    @Override
    public Produit addProduit(Produit produit, Long idUser) {
        User user = userRep.findById(idUser).orElse(null);
        Agriculteur agriculteur = (Agriculteur) user;
        produit.setAgriculteur(agriculteur);

        return produitrep.save(produit);

    }

    @Override
    public Produit updateProduit( Produit produit ,Long idProduit) {

        Produit existingProduit = produitrep.findById(idProduit).orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        existingProduit.setNomProduit(produit.getNomProduit());
        existingProduit.setPrixProduit(produit.getPrixProduit());
        existingProduit.setStockProduit(produit.getStockProduit());
        existingProduit.setDateFa(produit.getDateFa());
        existingProduit.setDateExp(produit.getDateExp());
        existingProduit.setCategorieProduit(produit.getCategorieProduit());
        existingProduit.setDescription(produit.getDescription());
        existingProduit.setImageProduit(produit.getImageProduit());
        existingProduit.setPrixReel(produit.getPrixReel());


        return produitrep.save(existingProduit);
    }



    @Override
    public Produit retriveProduit(Long idProduit) {
        if (idProduit == null) {
            throw new IllegalArgumentException("L'ID du produit ne peut pas être nul");
        }
        return produitrep.findById(idProduit).orElse(null);
    }

    @Override
    public void deletePiste(Long idProduit) {
       produitrep.deleteById(idProduit);
    }

    @Override
    public List<Produit> retriveAll() {
        return produitrep.findAll();
    }

    @Override
    public List<Produit> ListProduitByUser(Long idUser) {
        List<Produit> listd = produitrep.findAllByAgriculteur_IdUser(idUser);
        List<Produit> ListRes=new ArrayList<>();
        listd.forEach(produit -> {


                        ListRes.add(produit);
                }



        );
        return ListRes;    }

    @Override
    public List<Produit> ListProduitByStatusEnAttente() {
        StatusProduit enAttente = StatusProduit.En_attente;
        return produitrep.findAllByStatusProduit(enAttente);    }

    @Override
    public Produit updateStatusAndRemarque(Long idProduit, StatusProduit statusProduit, String remarque) {
        Produit produit = produitrep.findById(idProduit)
                .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé"));

        // Mettre à jour le statut et la remarque
        produit.setStatusProduit(statusProduit);
        produit.setRemarque(remarque);

        // Sauvegarder le produit mis à jour
        return produitrep.save(produit);
    }

    @Override
    public double calculerChiffreAffaires(Long idUser) {
        List<Produit> produits = ListProduitByUser(idUser);
        double total = 0;
        for (Produit produit : produits) {
            total += produit.getCountt() * produit.getPrixProduit();
        }
        return total;
    }

    @Override
    public double calculerCoutTotal(Long idUser) {
        List<Produit> produits = ListProduitByUser(idUser);
        double total = 0;
        for (Produit produit : produits) {
            total += produit.getCountt() * produit.getPrixReel();
        }
        return total;
    }

    @Override
    public double calculerBenefice(Long idUser) {
        return calculerChiffreAffaires(idUser) - calculerCoutTotal(idUser);    }

    @Override
    public double calculerMargeBeneficiaire(Long idUser) {
        double chiffreAffaires = calculerChiffreAffaires(idUser);
        if (chiffreAffaires > 0) {
            return (calculerBenefice(idUser) / chiffreAffaires) * 100;
        }
        return 0;
    }

    @Override
    public double calculerValeurStock(Long idUser) {
        List<Produit> produits = ListProduitByUser(idUser);
        double total = 0;
        for (Produit produit : produits) {
            total += produit.getStockProduit() * produit.getPrixReel();
        }
        return total;
    }

    @Override
    public List<Produit> getProduitsLesPlusVendus(Long idUser, int limit) {
        List<Produit> produits = ListProduitByUser(idUser);

        // Trier par nombre de ventes (countt) décroissant
        produits.sort((p1, p2) -> Integer.compare(p2.getCountt(), p1.getCountt()));

        // Retourner les N premiers produits (limit)
        if (produits.size() > limit) {
            return produits.subList(0, limit);
        }
        return produits;   //  il retourne les 5 premiers produits
    }

    @Override
    public Map<String, Map<String, Double>> getStatsParCategorie(Long idUser) {
        List<Produit> produits = ListProduitByUser(idUser);
        Map<String, Map<String, Double>> resultats = new HashMap<>();

        for (Produit produit : produits) {
            String categorie = produit.getCategorieProduit().toString();
            double revenue = produit.getCountt() * produit.getPrixProduit();
            double cout = produit.getCountt() * produit.getPrixReel();
            double benefice = revenue - cout;

            // Si la catégorie n'existe pas encore, l'initialiser
            if (!resultats.containsKey(categorie)) {
                Map<String, Double> stats = new HashMap<>();
                stats.put("revenue", 0.0);
                stats.put("cout", 0.0);
                stats.put("benefice", 0.0);
                resultats.put(categorie, stats);
            }
            Map<String, Double> catStats = resultats.get(categorie);
            catStats.put("revenue", catStats.get("revenue") + revenue);
            catStats.put("cout", catStats.get("cout") + cout);
            catStats.put("benefice", catStats.get("benefice") + benefice);
        }

        return resultats;
    }

    @Override
    public List<Produit> getProduitsFaibleStock(Long idUser, int pourcentage) {
        List<Produit> produits = ListProduitByUser(idUser);
        List<Produit> produitsFaibleStock = new ArrayList<>();

        for (Produit produit : produits) {
            // Calculer le pourcentage du stock vendu
            // Nous avons besoin du stock initial et du stock actuel
            int stockVendu = produit.getCountt(); // Nombre d'unités vendues
            int stockActuel = produit.getStockProduit(); // Stock restant
            int stockInitial = stockVendu + stockActuel; // Stock initial (estimation)

            if (stockInitial > 0) { // Éviter division par zéro
                double pourcentageVendu = (stockVendu * 100.0) / stockInitial;

                // Si le pourcentage vendu est supérieur ou égal à 50%
                if (pourcentageVendu >= pourcentage) {
                    produitsFaibleStock.add(produit);
                }
            }
        }

        return produitsFaibleStock;
    }

    @Override
    public Map<String, Object> getStatistiquesResume(Long idUser) {
        Map<String, Object> resume = new HashMap<>();

        // Statistiques financières
        resume.put("chiffreAffaires", calculerChiffreAffaires(idUser));
        resume.put("coutTotal", calculerCoutTotal(idUser));
        resume.put("benefice", calculerBenefice(idUser));
        resume.put("margeBeneficiaire", calculerMargeBeneficiaire(idUser));

        // Stock et inventaire
        resume.put("valeurStock", calculerValeurStock(idUser));
        resume.put("produitsFaibleStock", getProduitsFaibleStock(idUser, 50)); // 50% comme seuil

        // Top ventes et stats par catégorie
        resume.put("produitsTopVentes", getProduitsLesPlusVendus(idUser, 5));
        resume.put("statsParCategorie", getStatsParCategorie(idUser));

        return resume;
    }

    @Override
    @Scheduled(cron = "0 33 15 22 4 *")
    public void activerVenteFlash() {

        try {
            System.out.println("Vérification vente flash exécutée à: " + new Date());

            // Récupérer tous les produits disponibles
            List<Produit> produitsDisponibles = produitrep.findAllByStatusProduit(StatusProduit.Disponible);

            LocalDate today = LocalDate.now();
            LocalDate limitDate = today.plusDays(10);
            this.produitsVenteFlash = produitsDisponibles.stream()
                    .filter(produit -> {
                        LocalDate dateExp = convertToLocalDate(produit.getDateExp());
                        return dateExp != null && dateExp.isBefore(limitDate) && !dateExp.isBefore(today);
                    })
                    .collect(Collectors.toList());

            // Appliquer la réduction de 20% sur chaque produit
            for (Produit produit : produitsVenteFlash) {
                // Sauvegarder le prix original si ce n'est pas déjà fait

                produit.setPrixProduit(produit.getPrixProduit());


                // Appliquer la réduction de 20%
                int prixReduit = (int) (produit.getPrixProduit() * 0.8);
                produit.setPrixProduit(prixReduit);
                produit.setEnVenteFlash(true);

                // Sauvegarder les changements
                produitrep.save(produit);
            }

            // Activer la vente flash
            this.venteFlashActive = true;
            // Rest of your method...

        } catch (Exception e) {
            System.err.println("Error in activerVenteFlash: " + e.getMessage());
            e.printStackTrace();
        }
        // Filtrer les produits qui expirent dans moins de 10 jours


        // Programmer la désactivation pour la fin du dimanche (23:59:59)
        // En production, utilisez un autre @Scheduled pour cela
    }

    @Override
    public void desactiverVenteFlash() {
        if (this.produitsVenteFlash != null && !this.produitsVenteFlash.isEmpty()) {
            // Restaurer les prix originaux
            for (Produit produit : produitsVenteFlash) {
                if ( produit.getPrixProduit() > 0) {
                    produit.setPrixProduit(produit.getPrixProduit());
                }
                produit.setEnVenteFlash(false);
                produitrep.save(produit);
            }
        }

        // Désactiver la vente flash
        this.venteFlashActive = false;
        this.produitsVenteFlash = null;
    }

    @Override
    public List<Produit> getProduitsVenteFlash() {
        if (this.venteFlashActive && this.produitsVenteFlash != null) {
            return this.produitsVenteFlash;
        } else {
            // Si ce n'est pas dimanche, vérifier quand même les produits marqués en vente flash
            return produitrep.findAllByEnVenteFlashTrue();
        }
    }

    @Override
    public boolean isVenteFlashActive() {
        return LocalDate.now().getDayOfWeek().getValue() == 7 || this.venteFlashActive;
    }

    @Override
    public LocalDate convertToLocalDate(Date dateToConvert) {
        if (dateToConvert == null) {
            return null;
        }

        // Special handling for java.sql.Date
        if (dateToConvert instanceof java.sql.Date) {
            return ((java.sql.Date) dateToConvert).toLocalDate();
        }

        // For java.util.Date
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }


}
