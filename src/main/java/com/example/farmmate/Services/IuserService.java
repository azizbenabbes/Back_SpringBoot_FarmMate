package com.example.farmmate.Services;

import com.example.farmmate.Entities.Guser.Agriculteur;
import com.example.farmmate.Entities.Guser.Specialist;
import com.example.farmmate.Entities.Guser.User;

public interface IuserService {
    Agriculteur addUser(Agriculteur agriculteur);
    User getUserById(Long idUser);
    User signUp(User user);
    User signIn(String username, String password);
    Agriculteur addAgriculteur(Agriculteur agriculteur);
    Specialist addSpecialist(Specialist specialist);

}
