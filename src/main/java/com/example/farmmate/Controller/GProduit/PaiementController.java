package com.example.farmmate.Controller.GProduit;

import com.example.farmmate.Entities.GProduit.Panier;
import com.example.farmmate.Entities.GProduit.PanierProduit;
import com.example.farmmate.Entities.Guser.User;
import com.example.farmmate.Repositories.GProduit.PanierRep;
import com.example.farmmate.Repositories.GProduit.ProduitRep;
import com.example.farmmate.Repositories.UserRep;
import com.example.farmmate.Services.GProduit.AchatService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.*;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "*")

public class PaiementController {
    ProduitRep produitRep ;
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${app.frontend.url}")
    private String frontendUrl;
    @Autowired
    private  PanierRep panierRep;
    @Autowired
    private UserRep userRep;
    @Autowired
    private  AchatService achatService;

    @Autowired
    public void PaymentController(PanierRep panierRep, UserRep userRep, AchatService achatService) {
        this.panierRep = panierRep;
        this.userRep = userRep;
        this.achatService = achatService;
    }

    public PaiementController() {
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody Map<String, Object> payload) {
        Stripe.apiKey = stripeApiKey;

        try {
            // Récupération du montant depuis le payload
            long amount = Long.parseLong(payload.get("amount").toString());

            // Création des paramètres de la session Stripe Checkout
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(frontendUrl + "/dashboard/payment-success?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(frontendUrl + "/dashboard/payment-cancel")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("eur") // ou "eur"
                                                    .setUnitAmount(amount * 100) // Stripe utilise les centimes
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Achat FarmMate")
                                                                    .setDescription("Produits agricoles de FarmMate")
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            // Création de la session de paiement
            Session session = Session.create(params);

            // Retourner l'ID de session à utiliser côté client
            Map<String, String> responseData = new HashMap<>();
            responseData.put("clientSecret", session.getId());

            return ResponseEntity.ok(responseData);
        } catch (StripeException e) {
            Map<String, String> errorData = new HashMap<>();
            errorData.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorData);
        }
    }

    @GetMapping("/confirm/{sessionId}")
    public ResponseEntity<Map<String, Boolean>> confirmPayment(@PathVariable String sessionId) {
        Stripe.apiKey = stripeApiKey;

        try {
            // Récupérer les détails de la session pour vérifier le paiement
            Session session = Session.retrieve(sessionId);
            boolean isPaid = "paid".equals(session.getPaymentStatus());

            Map<String, Boolean> response = new HashMap<>();
            response.put("success", isPaid);

            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            Map<String, Boolean> errorData = new HashMap<>();
            errorData.put("success", false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorData);
        }
    }

    @PostMapping("/complete-purchase/{userId}")
    public ResponseEntity<Map<String, Object>> completePurchase(@PathVariable Long userId) {
        try {
            System.out.println("Début completePurchase avec userId: " + userId);

            User user = userRep.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            Panier panier = panierRep.findByUserId(userId);
            if (panier == null) {
                System.out.println("Panier introuvable");
                throw new RuntimeException("Panier vide ou non trouvé");
            }

            if (panier.getPanierProduits().isEmpty()) {
                System.out.println("Panier vide");
                throw new RuntimeException("Panier vide ou non trouvé");
            }

            long montantTotal = 0;
            for (PanierProduit pp : panier.getPanierProduits()) {
                montantTotal += pp.getProduit().getPrixProduit() * pp.getQuantite();
            }

            System.out.println("Montant total calculé : " + montantTotal);

            achatService.enregistrerAchat(user, panier.getPanierProduits(), montantTotal);
            System.out.println("Achat enregistré avec succès");

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Achat enregistré avec succès");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Voir l'erreur en console
            Map<String, Object> errorData = new HashMap<>();
            errorData.put("success", false);
            errorData.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorData);
        }
    }
     }
