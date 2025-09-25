package com.example.farmmate.Entities.GMangment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "workers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Worker implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workerId;

    private String name;
    private String contactNumber;
    private String email;

    private LocalDate dateOfEmployment;
    private String role;
    private int experienceYears;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;

    private float performanceRating;
    private String employmentStatus;
}
