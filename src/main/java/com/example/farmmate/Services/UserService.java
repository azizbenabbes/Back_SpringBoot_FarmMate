package com.example.farmmate.Services;


import com.example.farmmate.Entities.Guser.Agriculteur;
import com.example.farmmate.Entities.Guser.Role;
import com.example.farmmate.Entities.Guser.Specialist;
import com.example.farmmate.Entities.Guser.User;
import com.example.farmmate.Repositories.UserRep;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService  implements IuserService {
    @Autowired
    private UserRep userRepo;

    @Override
    public Agriculteur addUser(Agriculteur agriculteur) {
        return userRepo.save(agriculteur);
    }

    @Override
    public User getUserById(Long idUser) {
        return userRepo.findByIdUser(idUser);
    }

    @Override
    public User signUp(User user) {
        // Vérifier si l'utilisateur existe déjà
        User existingUser = userRepo.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("Nom d'utilisateur déjà utilisé");
        }

        // Vous pourriez ajouter une validation d'email ici
        existingUser = userRepo.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("Email déjà utilisé");
        }

        // Cryptage du mot de passe (à implémenter)
        // user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepo.save(user);
    }

    @Override
    public User signIn(String username, String password) {
        User user = userRepo.findByEmail(username);
        if (user == null) {
            throw new RuntimeException("Utilisateur non trouvé");
        }

        // Vérification du mot de passe (à implémenter avec un véritable encodeur)
        // if (!passwordEncoder.matches(password, user.getPassword())) {
        if (!user.getPassword().equals(password)) {  // Temporaire, à remplacer par un encodage sécurisé
            throw new RuntimeException("Mot de passe incorrect");
        }

        return user;
    }

    @Override
    public Agriculteur addAgriculteur(Agriculteur agriculteur) {


        agriculteur.setRole(Role.Agriculteur);
        return userRepo.save(agriculteur);
    }

    @Override
    public Specialist addSpecialist(Specialist specialist) {
        return userRepo.save(specialist);
    }


}
