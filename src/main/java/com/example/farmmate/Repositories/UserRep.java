package com.example.farmmate.Repositories;

import com.example.farmmate.Entities.Guser.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRep extends JpaRepository<User,Long> {
    User findByIdUser(Long id);
    User findByUsername(String username);
    User findByEmail(String email);

}
