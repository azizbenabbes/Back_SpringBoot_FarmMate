package com.example.farmmate.Services.GProduit;

import com.example.farmmate.Entities.GProduit.HistoriqueAchat;
import com.example.farmmate.Entities.GProduit.Produit;
import com.example.farmmate.Entities.GProduit.Reclamation;
import com.example.farmmate.Entities.Guser.User;
import com.example.farmmate.Repositories.GProduit.HistoriqueAchatRep;
import com.example.farmmate.Repositories.GProduit.ProduitRep;
import com.example.farmmate.Repositories.GProduit.ReclamationRep;
import com.example.farmmate.Repositories.UserRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class ReclamationService implements IreclamationService {
    @Autowired
    private ReclamationRep reclamationRepository;

    @Autowired
    private HistoriqueAchatRep historiqueAchatRep;

    @Autowired
    private ProduitRep produitRep;

    @Autowired
    private UserRep userRep;

    @Autowired
    private EmailService emailService;

    @Value("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")
    private String groqApiKey;

    @Value("https://api.groq.com/openai/v1/chat/completions")
    private String groqApiUrl;

    @Override
    public Reclamation creerReclamation(Long idHistorique, Long idProduit, Long userId, String contenu, Boolean iaGeneree) {
        HistoriqueAchat historiqueAchat = historiqueAchatRep.findById(idHistorique).orElse(null);
        Produit produit = produitRep.findById(idProduit).orElse(null);
        User client = userRep.findById(userId).orElse(null);

        if (historiqueAchat == null || produit == null || client == null) {
            throw new RuntimeException("Données invalides pour la création de la réclamation");
        }

        Reclamation reclamation = new Reclamation();
        reclamation.setHistoriqueAchat(historiqueAchat);
        reclamation.setProduit(produit);
        reclamation.setUserId(userId);
        reclamation.setContenuReclamation(contenu);
        reclamation.setDateReclamation(new Date());
        reclamation.setStatusReclamation("NOUVELLE");
        reclamation.setIaGeneree(iaGeneree);

        reclamation = reclamationRepository.save(reclamation);

        // Notifier l'agriculteur par email
        if (produit.getAgriculteur() != null && produit.getAgriculteur().getEmail() != null) {
            String emailAgriculteur = produit.getAgriculteur().getEmail();
            String sujet = "Nouvelle réclamation concernant votre produit sur FarmMate";
            String corps = "Bonjour " + produit.getAgriculteur().getName() + ",\n\n" +
                    "Une nouvelle réclamation a été enregistrée concernant votre produit '" + produit.getNomProduit() + "'.\n" +
                    "Détails de la réclamation :\n" +
                    "- Client : " + client.getName() + " " + client.getLastName() + "\n" +
                    "- Date : " + new Date() + "\n" +
                    "- Contenu : " + contenu + "\n\n" +
                    "Veuillez vous connecter à votre compte FarmMate pour gérer cette réclamation.\n\n" +
                    "Cordialement,\nL'équipe FarmMate";

            emailService.envoyerEmail(emailAgriculteur, sujet, corps);
        }

        return reclamation;  }

    @Override
    public List<Reclamation> getReclamationsParUtilisateur(Long userId) {
        return reclamationRepository.findByUserIdOrderByDateReclamationDesc(userId);
    }

    @Override
    public List<Reclamation> getReclamationsParAchat(Long idHistorique) {
        return reclamationRepository.findByHistoriqueAchatIdHistorique(idHistorique);
    }

    @Override
    public List<Reclamation> getReclamationsParAgriculteur(Long agriculteurId) {
        return reclamationRepository.findByProduit_Agriculteur_IdUser(agriculteurId);
    }

    @Override
    public Reclamation updateStatutReclamation(Long idReclamation, String nouveauStatut) {
        Reclamation reclamation = reclamationRepository.findById(idReclamation).orElse(null);
        if (reclamation != null) {
            reclamation.setStatusReclamation(nouveauStatut);
            return reclamationRepository.save(reclamation);
        }
        return null;
    }

    @Override
    public String genererReclamationAvecIA(Long userId, String problemeDescription, String produitNom) {
        User client = userRep.findById(userId).orElse(null);
        if (client == null) {
            throw new RuntimeException("Utilisateur non trouvé");
        }

        String nomClient = client.getName() + " " + client.getLastName();

        // Construction du prompt pour Llama 3.1
        String prompt = "Tu es un assistant spécialisé pour aider les clients de FarmMate à rédiger des réclamations concernant les produits agricoles. " +
                "Limite-toi uniquement à cette tâche: rédiger une réclamation professionnelle et courtoise. " +
                "Utilise les informations suivantes pour rédiger une réclamation formelle:\n\n" +
                "- Nom du client: " + nomClient + "\n" +
                "- Produit concerné: " + produitNom + "\n" +
                "- Description du problème par le client: \"" + problemeDescription + "\"\n\n" +
                "Rédige une réclamation formelle et professionnelle basée sur ces informations. " +
                "La réclamation doit être polie et constructive, tout en expliquant clairement le problème rencontré.";

        // Préparation de la requête pour l'API Groq
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama3-8b-8192");
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "Tu es un assistant spécialisé pour FarmMate qui aide uniquement à rédiger des réclamations."),
                Map.of("role", "user", "content", prompt)
        ));
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 1000);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    groqApiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    if (message != null) {
                        return (String) message.get("content");
                    }
                }
            }

            throw new RuntimeException("Impossible de générer la réclamation avec l'IA");
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la communication avec l'API Groq: " + e.getMessage(), e);
        }
    }

    @Override
    public Reclamation ajouterReponseReclamation(Long idReclamation, String reponse) {
        // Récupérer la réclamation par son ID
        Reclamation reclamation = reclamationRepository.findById(idReclamation)
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée"));

        // Ajouter la réponse
        reclamation.setReponseReclamation(reponse);
        reclamation.setDateReponse(new Date());


        // Mettre à jour le statut si nécessaire
        if ("NOUVELLE".equals(reclamation.getStatusReclamation())) {
            reclamation.setStatusReclamation("EN_TRAITEMENT");
        }

        // Enregistrer les modifications
        return reclamationRepository.save(reclamation);    }
}

