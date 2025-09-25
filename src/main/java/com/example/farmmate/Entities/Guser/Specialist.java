package com.example.farmmate.Entities.Guser;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Specialist extends User {
    @Enumerated(EnumType.STRING)
    private TypeSpecialiste specialite ;
}
