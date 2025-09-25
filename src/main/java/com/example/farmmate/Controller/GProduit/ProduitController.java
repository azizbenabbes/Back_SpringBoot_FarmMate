package com.example.farmmate.Controller.GProduit;

import com.example.farmmate.Entities.GProduit.Panier;
import com.example.farmmate.Entities.GProduit.Produit;
import com.example.farmmate.Entities.GProduit.StatusProduit;
import com.example.farmmate.Services.GProduit.PanierService;
import com.example.farmmate.Services.GProduit.ProduitService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("produit")
@AllArgsConstructor
@CrossOrigin("*")
public class ProduitController {
    @Autowired
    ProduitService produitService ;
    PanierService panierService ;

    @PostMapping("addProduit/{idUser}")
    public Produit addProduit(@RequestBody Produit produit,@PathVariable Long idUser) {
        return produitService.addProduit(produit, idUser);
    }
    @PutMapping("updateProduit/{idProduit}")

    public Produit updateProduit(@RequestBody Produit produit , @PathVariable Long idProduit) {
        return produitService.updateProduit(produit,idProduit);
    }
    @GetMapping("get/{idProduit}")
    public Produit retriveProduit(@PathVariable Long idProduit) {
        return produitService.retriveProduit(idProduit);
    }
    @DeleteMapping("delete/{idProduit}")
    public void deletePiste(@PathVariable Long idProduit) {
        produitService.deletePiste(idProduit);
    }
    @GetMapping("Diplayproduit")
    public List<Produit> retriveAll() {
        return produitService.retriveAll();
    }
    @GetMapping("ListProduitByUser/{id}")
    public List<Produit> ListProduitByUser (@PathVariable Long id)
    {return  produitService.ListProduitByUser(id) ; }
    @GetMapping("en-attente")
    public List<Produit> getProduitsEnAttente() {
        return produitService.ListProduitByStatusEnAttente();
}


    @PutMapping("/updateStatusAndRemarque/{idProduit}")
    public Produit updateStatusAndRemarque(
            @PathVariable Long idProduit,
            @RequestParam StatusProduit statusProduit,
            @RequestParam String remarque) {

        return produitService.updateStatusAndRemarque(idProduit, statusProduit, remarque);
    }
    @GetMapping("/produits")
    public ResponseEntity<List<Produit>> getProduitsVenteFlash() {
        List<Produit> produits = produitService.getProduitsVenteFlash();
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getVenteFlashStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("active", produitService.isVenteFlashActive());
        response.put("nombreProduits", produitService.getProduitsVenteFlash().size());
        return ResponseEntity.ok(response);
    }

    // Pour les tests, permettre l'activation manuelle de la vente flash
    @GetMapping("/activer")
    public ResponseEntity<String> activerVenteFlash() {
        produitService.activerVenteFlash();
        return ResponseEntity.ok("Vente flash activée avec succès");
    }

    // Pour les tests, permettre la désactivation manuelle de la vente flash
    @GetMapping("/desactiver")
    public ResponseEntity<String> desactiverVenteFlash() {
        produitService.desactiverVenteFlash();
        return ResponseEntity.ok("Vente flash désactivée avec succès");
    }
}
