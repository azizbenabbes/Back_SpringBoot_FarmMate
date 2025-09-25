package com.example.farmmate.Controller;

import com.example.farmmate.Entities.Guser.Agriculteur;
import com.example.farmmate.Entities.Guser.Specialist;
import com.example.farmmate.Entities.Guser.User;
import com.example.farmmate.Services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("user")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userServ;

    @PostMapping("addAgriculteur")
    public Agriculteur addAgriculteur(@RequestBody Agriculteur agriculteur) {
        return userServ.addAgriculteur(agriculteur);
    }

    @PostMapping("addSpecialist")
    public Specialist addSpecialist(@RequestBody Specialist specialist) {
        return userServ.addSpecialist(specialist);
    }

    @GetMapping("/getUserById/{idUser}")
    public User getUserById(@PathVariable Long idUser) {
        return userServ.getUserById(idUser);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user) {
        try {
            User newUser = userServ.signUp(user);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Inscription réussie");
            response.put("user", newUser);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody Map<String, String> credentials, HttpSession session) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");

            User user = userServ.signIn(email, password);

            // Stocker l'ID utilisateur dans la session
            session.setAttribute("userId", user.getIdUser());
            session.setAttribute("userRole", user.getRole().toString());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Connexion réussie");
            response.put("user", user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut(HttpSession session) {
        session.invalidate();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Déconnexion réussie");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/current-user")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Non authentifié");
            return ResponseEntity.status(401).body(response);
        }

        User user = userServ.getUserById(userId);
        return ResponseEntity.ok(user);
    }

}
