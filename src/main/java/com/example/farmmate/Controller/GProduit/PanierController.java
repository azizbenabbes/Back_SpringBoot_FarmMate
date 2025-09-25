package com.example.farmmate.Controller.GProduit;

import com.example.farmmate.Entities.GProduit.Panier;
import com.example.farmmate.Entities.GProduit.PanierProduit;
import com.example.farmmate.Entities.GProduit.Produit;
import com.example.farmmate.Services.GProduit.PanierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("panier")
@CrossOrigin("*")

public class PanierController {
    @Autowired

    PanierService panierService ;
    @PostMapping("addPanier/{idUsersession}/{idproduit}")
    public Panier addPanier( @PathVariable Long idUsersession, @PathVariable Long idproduit,@RequestBody Map<String, Integer> requestBody) {
        int quantitePanier = requestBody.get("quantitePanier");

        return panierService.addPanier(idUsersession,idproduit,quantitePanier);
    }
    @PutMapping ("modifierQuantitePanier/{idUsersession}/{idproduit}")

    public Panier modifierQuantitePanier(@PathVariable Long idUsersession, @PathVariable Long idproduit, @RequestBody Map<String, Integer> requestBody) {
        int nouvelleQuantite = requestBody.get("quantitePanier");
        return panierService.modifierQuantitePanier(idUsersession,idproduit, nouvelleQuantite);

    }

    @PutMapping("updatePanier")


    public Panier updatePanier(@RequestBody Panier panier) {
        return panierService.updatePanier(panier);
    }
    @GetMapping("get/{idPanier}")
    public Panier retrivePanier(Long idPanier) {
        return panierService.retrivePanier(idPanier);
    }
    @DeleteMapping("delete/{idPanier}")

    public void deletePanier(Long idPanier) {
        panierService.deletePanier(idPanier);

    }

    public List<Panier> retriveAll() {
        return panierService.retriveAll();
    }
    @GetMapping("calculerTotalPanier/{idUsersession}")

    public long  calculerTotalPanier(@PathVariable Long idUsersession) {
        return panierService.calculerTotalPanier(idUsersession);
    }
    @DeleteMapping("supprimerProduitDuPanier/{idUsersession}/{idproduit}")
    public Panier supprimerProduitDuPanier(@PathVariable Long idUsersession, @PathVariable Long idproduit){
        return  panierService.supprimerProduitDuPanier(idUsersession,idproduit);
    }
    @GetMapping("/getProduitsDuPanier/{idUsersession}")
    public List<Produit> getProduitsDuPanier(@PathVariable Long idUsersession) {
        return panierService.getProduitsDuPanier(idUsersession);

    }
    @PostMapping("/personalized")
    public ResponseEntity<?> createPersonalizedCart(@RequestBody Map<String, Object> request) {
        // Vérifier si les champs requis existent
        if (request.get("userId") == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Le paramètre 'userId' est requis"));
        }
        if (request.get("budget") == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Le paramètre 'budget' est requis"));
        }

        // Extraire les valeurs avec une gestion de null sécurisée
        Long userId;
        int budget;
        String category = null;

        try {
            userId = Long.valueOf(String.valueOf(request.get("userId")));
            budget = Integer.parseInt(String.valueOf(request.get("budget")));

            // Catégorie est optionnelle, on la traite séparément
            if (request.get("category") != null) {
                category = String.valueOf(request.get("category"));
                // Si category est une chaîne vide, on la considère comme null
                if (category.isEmpty()) {
                    category = null;
                }
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Format de données invalide: " + e.getMessage()));
        }

        try {
            Map<String, Object> result = panierService.createPersonalizedCart(userId, budget, category);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/getProduitsDuPanierWithQuantity/{idUsersession}")

    public List<Map<String, Object>> getProduitsDuPanierWithQuantity(@PathVariable Long idUsersession) {

        return panierService.getProduitsDuPanierWithQuantity(idUsersession) ;
    }
    }
