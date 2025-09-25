package com.example.farmmate.Services.GProduit;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service

public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine; // Pour le rendu des templates HTML
    public void envoyerEmail(String destinataire, String sujet, String corps) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(destinataire);
        message.setSubject(sujet);
        message.setText(corps);
        mailSender.send(message);
    }

    public void envoyerEmailHtml(String destinataire, String sujet, String contenuHtml) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(destinataire);
        helper.setSubject(sujet);
        helper.setText(contenuHtml, true); // Le deuxième paramètre indique que c'est du HTML

        // Ajouter le logo FarmMate comme pièce jointe inline
        helper.addInline("logo-farmmate", new ClassPathResource("static/assets/img/logo.jpg"));

        mailSender.send(message);
    }

    /**
     * Envoie un email basé sur un template Thymeleaf avec des données variables
     */
    public void envoyerEmailTemplate(String destinataire, String sujet, String templateName, Map<String, Object> variables) throws MessagingException {
        // Préparer le contexte avec les variables pour le template
        Context context = new Context();
        context.setVariables(variables);

        // Générer le HTML à partir du template
        String htmlContent = templateEngine.process(templateName, context);

        // Envoyer l'email HTML
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(destinataire);
        helper.setSubject(sujet);
        helper.setText(htmlContent, true);

        // Ajouter le logo FarmMate comme pièce jointe inline
        helper.addInline("logo-farmmate", new ClassPathResource("static/assets/img/logo.jpg"));

        mailSender.send(message);
    }
}
