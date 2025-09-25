package com.example.farmmate.Controller.GProduit;

import com.example.farmmate.Entities.GProduit.Reclamation;
import com.example.farmmate.Services.GProduit.ReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("reclamations")
@CrossOrigin(origins = "*")
public class ReclamationController {
    @Autowired
    private ReclamationService reclamationService;

    @PostMapping("/creer")
    public ResponseEntity<Reclamation> creerReclamation(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("Requête reçue: " + request);

            Long idHistorique = Long.valueOf(request.get("idHistorique").toString());
            Long idProduit = Long.valueOf(request.get("idProduit").toString());
            Long userId = Long.valueOf(request.get("userId").toString());
            String contenu = (String) request.get("contenu");
            Boolean iaGeneree = (Boolean) request.get("iaGeneree");

            System.out.println("Valeurs analysées - idHistorique: " + idHistorique +
                    ", idProduit: " + idProduit +
                    ", userId: " + userId +
                    ", iaGeneree: " + iaGeneree);

            Reclamation reclamation = reclamationService.creerReclamation(
                    idHistorique, idProduit, userId, contenu, iaGeneree);

            return new ResponseEntity<>(reclamation, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace(); // Afficher la trace complète de la pile
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/utilisateur/{userId}")
    public ResponseEntity<List<Reclamation>> getReclamationsParUtilisateur(@PathVariable Long userId) {
        try {
            List<Reclamation> reclamations = reclamationService.getReclamationsParUtilisateur(userId);
            return new ResponseEntity<>(reclamations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/achat/{idHistorique}")
    public ResponseEntity<List<Reclamation>> getReclamationsParAchat(@PathVariable Long idHistorique) {
        try {
            List<Reclamation> reclamations = reclamationService.getReclamationsParAchat(idHistorique);
            return new ResponseEntity<>(reclamations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/agriculteur/{agriculteurId}")
    public ResponseEntity<List<Reclamation>> getReclamationsParAgriculteur(@PathVariable Long agriculteurId) {
        try {
            List<Reclamation> reclamations = reclamationService.getReclamationsParAgriculteur(agriculteurId);
            return new ResponseEntity<>(reclamations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/statut/{idReclamation}")
    public ResponseEntity<Reclamation> updateStatutReclamation(
            @PathVariable Long idReclamation,
            @RequestBody Map<String, String> request) {
        try {
            String nouveauStatut = request.get("statut");
            Reclamation reclamation = reclamationService.updateStatutReclamation(idReclamation, nouveauStatut);

            if (reclamation != null) {
                return new ResponseEntity<>(reclamation, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/generer-ia")
    public ResponseEntity<Map<String, String>> genererReclamationAvecIA(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            String problemeDescription = (String) request.get("problemeDescription");
            String produitNom = (String) request.get("produitNom");

            String reclamationGeneree = reclamationService.genererReclamationAvecIA(
                    userId, problemeDescription, produitNom);

            return new ResponseEntity<>(Map.of("reclamation", reclamationGeneree), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("erreur", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/reponse/{idReclamation}")
    public ResponseEntity<Reclamation> envoyerReponseReclamation(
            @PathVariable Long idReclamation,
            @RequestBody Map<String, String> request) {
        try {
            String reponse = request.get("reponse");

            if (reponse == null || reponse.trim().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            Reclamation reclamation = reclamationService.ajouterReponseReclamation(idReclamation, reponse);

            if (reclamation != null) {
                return new ResponseEntity<>(reclamation, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
