package com.example.farmmate.Services.GProduit;

import com.example.farmmate.Entities.GProduit.*;
import com.example.farmmate.Entities.Guser.User;
import com.example.farmmate.Repositories.GProduit.PanierProduitRep;
import com.example.farmmate.Repositories.GProduit.PanierRep;
import com.example.farmmate.Repositories.GProduit.ProduitRep;
import com.example.farmmate.Repositories.UserRep;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PanierService implements IpanierService{


    @Autowired
    PanierRep panierRep;
    ProduitRep produitrep ;
    UserRep userRep ;
    PanierProduitRep panierProduitRep ;





    @Override
    public Panier addPanier( Long idUsersession, Long idproduit,int quantitePanier) {
        // Récupérer le produit
        Produit produit = produitrep.findById(idproduit).orElse(null);
        if (produit == null) {
            throw new RuntimeException("Produit non trouvé pour l'ID: " + idproduit);
        }

        // Vérifier que la quantité est entre 10 et 500 kg
        if (quantitePanier < 10 || quantitePanier > 500) {
            throw new IllegalArgumentException("La quantité doit être comprise entre 10 et 500 kg.");
        }

        // Vérifier que la quantité demandée ne dépasse pas le stock disponible
        if (quantitePanier > produit.getStockProduit()) {
            throw new IllegalArgumentException("La quantité demandée dépasse le stock disponible.");
        }

        // Vérifier si un panier existe déjà pour cet utilisateur
        Panier panier = panierRep.findByUserId(idUsersession);
        if (panier == null) {
            // Si aucun panier n'existe, en créer un nouveau
            panier = new Panier();
            panier.setUserId(idUsersession);
            panier.setDateCreation(Date.valueOf(LocalDate.now()));
            panier = panierRep.save(panier);  // Sauvegarder d'abord le panier
        }

        // Vérifier si le produit est déjà dans le panier
        boolean produitDejaDansPanier = panier.getPanierProduits().stream()
                .anyMatch(pp -> pp.getProduit().getIdProduit().equals(produit.getIdProduit()));

        if (produitDejaDansPanier) {
            throw new IllegalArgumentException("Ce produit est déjà présent dans votre panier.");
        }

        // Créer un nouvel objet PanierProduit pour la jointure avec la quantité
        PanierProduit panierProduit = new PanierProduit();
        panierProduit.setPanier(panier);
        panierProduit.setProduit(produit);
        panierProduit.setQuantite(quantitePanier);

        // Réduire le stock du produit en fonction de la quantité ajoutée au panier
        produit.setStockProduit(produit.getStockProduit() - quantitePanier);
        produit.setCountt(quantitePanier);

        // Sauvegarder le panierProduit (ajout dans la table de jointure)
        panierProduitRep.save(panierProduit);

        // Sauvegarder le produit avec le stock mis à jour
        produitrep.save(produit);

        // Sauvegarder le panier
        return panierRep.save(panier);
    }


    @Override
    public Panier updatePanier(Panier panier) {
        return panierRep.save(panier);
    }

    @Override
    public Panier retrivePanier(Long idPanier) {
        return panierRep.findById(idPanier).orElse(null);
    }

    @Override
    public void deletePanier(Long idPanier) {
        panierRep.deleteById(idPanier);

    }

    @Override
    public List<Panier> retriveAll() {
        return panierRep.findAll();
    }

    @Override
    public long calculerTotalPanier(Long idUsersession) {
        // Récupérer le panier de l'utilisateur
        Panier panier = panierRep.findByUserId(idUsersession);
        if (panier == null) {
            throw new RuntimeException("Panier non trouvé pour l'utilisateur ID: " + idUsersession);
        }

        // Initialiser la somme totale
        long prixTotal = 0;

        // Parcourir les produits du panier (en utilisant la table de jointure PanierProduit)
        for (PanierProduit panierProduit : panier.getPanierProduits()) {
            Produit produit = panierProduit.getProduit(); // Récupérer le produit
            int quantite = panierProduit.getQuantite(); // Quantité spécifique pour ce produit dans le panier

            // Calculer le prix du produit (quantité * prix unitaire)
            long prixProduit = produit.getPrixProduit() * quantite;

            // Ajouter le prix du produit à la somme totale
            prixTotal += prixProduit;
        }

        return prixTotal;   }


    @Override
    public Panier supprimerProduitDuPanier(Long idUsersession, Long idproduit) {
        // Récupérer le panier de l'utilisateur
        Panier panier = panierRep.findByUserId(idUsersession);
        if (panier == null) {
            throw new RuntimeException("Panier non trouvé pour l'utilisateur ID: " + idUsersession);
        }

        // Récupérer le produit
        Produit produit = produitrep.findById(idproduit).orElse(null);
        if (produit == null) {
            throw new RuntimeException("Produit non trouvé pour l'ID: " + idproduit);
        }

        // Vérifier si le produit est dans le panier
        PanierProduit panierProduitASupprimer = null;
        boolean produitTrouve = false;

        // Parcourir les produits dans la table de jointure pour trouver celui à supprimer
        for (PanierProduit panierProduit : panier.getPanierProduits()) {
            if (panierProduit.getProduit().getIdProduit().equals(idproduit)) {
                panierProduitASupprimer = panierProduit;
                produitTrouve = true;

                // Restaurer le stock du produit
                produit.setStockProduit(produit.getStockProduit() + panierProduit.getQuantite());
                produit.setCountt(produit.getCountt()-panierProduit.getQuantite());

                produitrep.save(produit); // Sauvegarder la mise à jour du stock

                break;
            }
        }

        if (!produitTrouve) {
            throw new RuntimeException("Le produit n'est pas dans le panier.");
        }

        // Supprimer l'entrée de la table de jointure (PanierProduit)
        panier.getPanierProduits().remove(panierProduitASupprimer);
        panierProduitRep.delete(panierProduitASupprimer);

        // Vérifier si le panier est vide après la suppression du produit
        if (panier.getPanierProduits().isEmpty()) {
            // Si le panier est vide, supprimer le panier
            panierRep.delete(panier);
            return null; // Retourne null, car le panier a été supprimé
        }

        // Sauvegarder les modifications du panier
        return panierRep.save(panier);
}

    @Override
    public List<Produit> getProduitsDuPanier(Long idUsersession) {
        // Récupérer le panier de l'utilisateur
        Panier panier = panierRep.findByUserId(idUsersession);
        if (panier == null) {
            throw new RuntimeException("Panier non trouvé pour l'utilisateur ID: " + idUsersession);
        }

        // Créer une liste de produits vide
        List<Produit> produitsDuPanier = new ArrayList<>();

        // Parcourir les éléments de la table de jointure pour récupérer les produits associés
        for (PanierProduit panierProduit : panier.getPanierProduits()) {
            produitsDuPanier.add(panierProduit.getProduit()); // Ajouter le produit à la liste
        }

        // Retourner la liste des produits du panier
        return produitsDuPanier;
    }

    @Override
    public Panier modifierQuantitePanier(Long idUsersession, Long idproduit, int nouvelleQuantite) {
        // Récupérer le produit
        Produit produit = produitrep.findById(idproduit).orElse(null);
        if (produit == null) {
            throw new RuntimeException("Produit non trouvé pour l'ID: " + idproduit);
        }

        // Vérifier que la quantité est entre 10 et 500 kg
        if (nouvelleQuantite < 10 || nouvelleQuantite > 500) {
            throw new IllegalArgumentException("La quantité doit être comprise entre 10 et 500 kg.");
        }

        // Vérifier que la quantité demandée ne dépasse pas le stock disponible
        if (nouvelleQuantite > produit.getStockProduit()) {
            throw new IllegalArgumentException("La quantité demandée dépasse le stock disponible.");
        }

        // Récupérer le panier existant pour cet utilisateur
        Panier panier = panierRep.findByUserId(idUsersession);
        if (panier == null) {
            throw new RuntimeException("Aucun panier trouvé pour l'utilisateur ID: " + idUsersession);
        }

        // Vérifier si le produit est présent dans le panier
        PanierProduit panierProduitDansPanier = panier.getPanierProduits().stream()
                .filter(p -> p.getProduit().getIdProduit().equals(idproduit))
                .findFirst()
                .orElse(null);

        if (panierProduitDansPanier == null) {
            throw new RuntimeException("Produit non trouvé dans le panier.");
        }

        // Récupérer l'ancienne quantité dans le panier
        int ancienneQuantite = panierProduitDansPanier.getQuantite();

        // Mettre à jour la quantité du produit dans le panier
        panierProduitDansPanier.setQuantite(nouvelleQuantite);

        // Mettre à jour le stock du produit
        // Ajouter l'ancienne quantité au stock
        produit.setStockProduit(produit.getStockProduit() + ancienneQuantite);

        // Réduire le stock du produit en fonction de la nouvelle quantité
        produit.setStockProduit(produit.getStockProduit() - nouvelleQuantite);

        // Sauvegarder les modifications du produit
        produitrep.save(produit); // Sauvegarder le produit avec la nouvelle quantité

        // Sauvegarder uniquement le panier si nécessaire
        panierRep.save(panier); // Sauvegarder le panier avec le produit mis à jour

        return panier; // Retourner le panier mis à jour
}

    @Override
    public Map<String, Object> createPersonalizedCart(Long userId, int budget, String category) {
        if (budget < 20) {
            throw new RuntimeException("Le budget minimum doit être de 20€.");
        }

        User user = userRep.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("Utilisateur non trouvé.");
        }

        Panier panier = panierRep.findByUserId(userId);
        if (panier == null) {
            panier = new Panier();
            panier.setUserId(userId);
            panier.setDateCreation(Date.valueOf(LocalDate.now()));
            panier = panierRep.save(panier);
        }

        Set<Long> produitsDejaPresents = panier.getPanierProduits() == null ? new HashSet<>() :
                panier.getPanierProduits().stream()
                        .map(pp -> pp.getProduit().getIdProduit())
                        .collect(Collectors.toSet());

        List<Produit> produitsDisponibles = produitrep.findAll().stream()
                .filter(p -> p.getStatusProduit() == StatusProduit.Disponible)
                .filter(p -> p.getStockProduit() > 0)
                .filter(p -> p.getDateExp().after(Date.valueOf(LocalDate.now())))
                .filter(p -> !produitsDejaPresents.contains(p.getIdProduit()))
                .collect(Collectors.toList());

        if (category != null && !category.isEmpty()) {
            try {
                CategorieProduit cat = CategorieProduit.valueOf(category);
                produitsDisponibles = produitsDisponibles.stream()
                        .filter(p -> p.getCategorieProduit() == cat)
                        .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Catégorie invalide: " + category);
            }
        }

        if (produitsDisponibles.isEmpty()) {
            throw new RuntimeException("Aucun produit disponible.");
        }

        // Calculer le stock moyen pour déterminer le seuil de 50%
        double stockMoyen = produitsDisponibles.stream()
                .mapToInt(Produit::getStockProduit)
                .average()
                .orElse(0);
        double seuilStock = stockMoyen * 0.5;

        // Date actuelle pour calculer la proximité de la date d'expiration
        Date aujourdHui = Date.valueOf(LocalDate.now());

        // Assigner un score à chaque produit en fonction de sa date d'expiration et son stock
        Map<Produit, Double> produitsAvecScore = new HashMap<>();

        for (Produit produit : produitsDisponibles) {
            // Calculer la différence en jours entre aujourd'hui et la date d'expiration
            long diffInMillies = produit.getDateExp().getTime() - aujourdHui.getTime();
            long diffJours = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            // Formule de score: plus la date d'expiration est proche, plus le score est élevé
            // Le score diminue exponentiellement avec le nombre de jours avant expiration
            double scoreExpiration = Math.exp(-0.1 * diffJours); // Fonction exponentielle décroissante

            // Bonus pour les produits avec stock élevé
            double scoreStock = produit.getStockProduit() > seuilStock ? 1.5 : 1.0;

            // Score final = combinaison des deux facteurs
            double scoreFinal = scoreExpiration * scoreStock;

            produitsAvecScore.put(produit, scoreFinal);
        }

        // Trier les produits par score décroissant
        List<Produit> produitsTries = produitsAvecScore.entrySet().stream()
                .sorted(Map.Entry.<Produit, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Distribution du budget basée sur le score
        double scoreTotal = produitsAvecScore.values().stream().mapToDouble(Double::doubleValue).sum();
        Map<Produit, Integer> budgetParProduit = new HashMap<>();

        for (Produit produit : produitsTries) {
            double pourcentageBudget = produitsAvecScore.get(produit) / scoreTotal;
            int budgetAlloue = (int) (budget * pourcentageBudget);
            budgetParProduit.put(produit, Math.max(budgetAlloue, 1)); // Au moins 1€ par produit
        }

        int budgetRestant = budget;
        List<Long> addedProductIds = new ArrayList<>();
        Map<Long, Integer> produitQuantites = new HashMap<>();
        int totalSpent = 0;

        // Quantité minimale pour les produits prioritaires
        final int quantiteMinimale = 10;

        // Traiter d'abord les produits triés par priorité
        for (Produit produit : produitsTries) {
            int prix = produit.getPrixProduit();
            int stockDisponible = produit.getStockProduit();
            int budgetAlloue = budgetParProduit.get(produit);

            // Si le budget alloué est insuffisant, utiliser le budget restant
            budgetAlloue = Math.min(budgetAlloue, budgetRestant);

            if (budgetAlloue < prix) {
                continue; // Passer au produit suivant si on ne peut pas en acheter au moins un
            }

            // Calculer le nombre d'unités en fonction du budget alloué
            int nombreUnites = Math.min(stockDisponible, budgetAlloue / prix);

            // Pour les produits prioritaires (début de la liste), essayer d'atteindre la quantité minimale
            if (produitsTries.indexOf(produit) < produitsTries.size() / 3) { // Premier tiers = prioritaires
                long diffInMillies = produit.getDateExp().getTime() - aujourdHui.getTime();
                long diffJours = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                if (diffJours < 14 && stockDisponible >= quantiteMinimale && (prix * quantiteMinimale) <= budgetRestant) {
                    nombreUnites = Math.max(nombreUnites, quantiteMinimale);
                }
            }

            if (nombreUnites > 0) {
                // Ajouter le produit au panier
                PanierProduit pp = new PanierProduit();
                pp.setPanier(panier);
                pp.setProduit(produit);
                pp.setQuantite(nombreUnites);
                panierProduitRep.save(pp);

                // Mettre à jour le stock et la popularité du produit
                produit.setStockProduit(produit.getStockProduit() - nombreUnites);
                produit.setCountt(nombreUnites);
                produitrep.save(produit);

                // Mettre à jour les statistiques
                int coutTotal = nombreUnites * prix;
                addedProductIds.add(produit.getIdProduit());
                produitQuantites.put(produit.getIdProduit(), nombreUnites);
                totalSpent += coutTotal;
                budgetRestant -= coutTotal;
            }

            // Arrêter si le budget est épuisé
            if (budgetRestant < prix) {
                break;
            }
        }

        // S'il reste du budget, faire un deuxième passage pour essayer de l'utiliser complètement
        if (budgetRestant > 0) {
            for (Produit produit : produitsTries) {
                // Sauter les produits déjà dans le panier
                if (produitQuantites.containsKey(produit.getIdProduit())) {
                    continue;
                }

                int prix = produit.getPrixProduit();
                if (budgetRestant >= prix && produit.getStockProduit() > 0) {
                    int unitesPossibles = Math.min(produit.getStockProduit(), budgetRestant / prix);

                    if (unitesPossibles > 0) {
                        // Ajouter le produit au panier
                        PanierProduit pp = new PanierProduit();
                        pp.setPanier(panier);
                        pp.setProduit(produit);
                        pp.setQuantite(unitesPossibles);
                        panierProduitRep.save(pp);

                        // Mettre à jour le stock et la popularité du produit
                        produit.setStockProduit(produit.getStockProduit() - unitesPossibles);
                        produit.setCountt(unitesPossibles);
                        produitrep.save(produit);

                        // Mettre à jour les statistiques
                        int coutTotal = unitesPossibles * prix;
                        addedProductIds.add(produit.getIdProduit());
                        produitQuantites.put(produit.getIdProduit(), unitesPossibles);
                        totalSpent += coutTotal;
                        budgetRestant -= coutTotal;
                    }
                }

                if (budgetRestant < prix) {
                    break;
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("produits", produitrep.findAllById(addedProductIds));
        result.put("quantites", produitQuantites);
        result.put("totalDepense", totalSpent);
        result.put("nombreProduits", addedProductIds.size());

        return result; }

    @Override
    public List<Map<String, Object>> getProduitsDuPanierWithQuantity(Long idUsersession) {
        // Récupérer le panier de l'utilisateur
        Panier panier = panierRep.findByUserId(idUsersession);
        if (panier == null) {
            throw new RuntimeException("Panier non trouvé pour l'utilisateur ID: " + idUsersession);
        }

        // Créer une liste de produits avec leur quantité
        List<Map<String, Object>> produitsAvecQuantite = new ArrayList<>();

        // Parcourir les éléments de la table de jointure
        for (PanierProduit panierProduit : panier.getPanierProduits()) {
            Map<String, Object> produitMap = new HashMap<>();

            // Récupérer le produit
            Produit produit = panierProduit.getProduit();

            // Ajouter le produit et sa quantité à la map
            produitMap.put("produit", produit);
            produitMap.put("quantitePanier", panierProduit.getQuantite());

            produitsAvecQuantite.add(produitMap);
        }

        return produitsAvecQuantite;
    }


}

