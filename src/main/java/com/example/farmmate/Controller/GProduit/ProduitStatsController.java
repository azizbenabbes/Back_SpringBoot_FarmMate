package com.example.farmmate.Controller.GProduit;
import com.example.farmmate.Entities.GProduit.Produit;
import com.example.farmmate.Services.GProduit.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/produit/stats")
@CrossOrigin("*")
public class ProduitStatsController {
    @Autowired
    private ProduitService produitService;

    @GetMapping("/completes/{idUser}")
    public ResponseEntity<Map<String, Object>> getStatistiquesResume(@PathVariable Long idUser) {
        try {
            Map<String, Object> stats = produitService.getStatistiquesResume(idUser);
            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/chiffre-affaires/{idUser}")
    public ResponseEntity<Double> getChiffreAffaires(@PathVariable Long idUser) {
        try {
            double chiffreAffaires = produitService.calculerChiffreAffaires(idUser);
            return new ResponseEntity<>(chiffreAffaires, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/benefice/{idUser}")
    public ResponseEntity<Double> getBenefice(@PathVariable Long idUser) {
        try {
            double benefice = produitService.calculerBenefice(idUser);
            return new ResponseEntity<>(benefice, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/marge/{idUser}")
    public ResponseEntity<Double> getMargeBeneficiaire(@PathVariable Long idUser) {
        try {
            double marge = produitService.calculerMargeBeneficiaire(idUser);
            return new ResponseEntity<>(marge, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/valeur-stock/{idUser}")
    public ResponseEntity<Double> getValeurStock(@PathVariable Long idUser) {
        try {
            double valeurStock = produitService.calculerValeurStock(idUser);
            return new ResponseEntity<>(valeurStock, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/top-ventes/{idUser}")
    public ResponseEntity<List<Produit>> getTopVentes(
            @PathVariable Long idUser,
            @RequestParam(defaultValue = "5") int limit) {
        try {
            List<Produit> topProduits = produitService.getProduitsLesPlusVendus(idUser, limit);
            return new ResponseEntity<>(topProduits, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/faible-stock/{idUser}")
    public ResponseEntity<List<Produit>> getProduitsFaibleStock(
            @PathVariable Long idUser,
            @RequestParam(defaultValue = "5") int seuil) {
        try {
            List<Produit> produitsFaibleStock = produitService.getProduitsFaibleStock(idUser, seuil);
            return new ResponseEntity<>(produitsFaibleStock, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/stats-categories/{idUser}")
    public ResponseEntity<Map<String, Map<String, Double>>> getStatsCategories(@PathVariable Long idUser) {
        try {
            Map<String, Map<String, Double>> statsCategories = produitService.getStatsParCategorie(idUser);
            return new ResponseEntity<>(statsCategories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
