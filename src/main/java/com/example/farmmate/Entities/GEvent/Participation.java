package com.example.farmmate.Entities.GEvent;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Participation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idParticipation;

    private long numTicket;
    private Date dateInscription;



    @ManyToOne
    @JoinColumn(name = "evenement_id")
    private Evenement evenement_id;

}



