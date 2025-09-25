package com.example.farmmate.Controller.GProduit;

import com.example.farmmate.Entities.GProduit.HistoriqueAchat;
import com.example.farmmate.Services.GProduit.AchatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("Achat")
@CrossOrigin("*")
public class AchatController {
    @Autowired
    private AchatService achatService;
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HistoriqueAchat>> getHistoriqueAchatsByUser(@PathVariable Long userId) {
        List<HistoriqueAchat> historiqueAchats = achatService.getHistoriqueAchatsByUser(userId);
        return new ResponseEntity<>(historiqueAchats, HttpStatus.OK);
    }

    @GetMapping("/{idHistorique}")
    public ResponseEntity<HistoriqueAchat> getHistoriqueAchatById(@PathVariable Long idHistorique) {
        HistoriqueAchat historiqueAchat = achatService.getHistoriqueAchatById(idHistorique);
        if (historiqueAchat != null) {
            return new ResponseEntity<>(historiqueAchat, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
