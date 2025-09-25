package com.example.farmmate.Entities.Guser;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("agriculteur")

public class Agriculteur extends User{
}
