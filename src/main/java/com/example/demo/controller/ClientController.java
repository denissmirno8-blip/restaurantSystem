package com.example.demo.controller;

import com.example.demo.repository.Client;
import com.example.demo.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(path = "/api/clients")
public class ClientController {

    private final ClientService clientService;


    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<Client>> getClients(){
        return ResponseEntity.ok(clientService.findAll());
    }

    @PostMapping
    public ResponseEntity<Client> create(@Valid @RequestBody Client client){
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.create(client));
    }

    @PostMapping(path="all")
    public ResponseEntity<String> createMultiple(@Valid @RequestBody List<Client> clients) {
        for (Client client : clients) {
            clientService.create(client);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("All clients are created.");
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        clientService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping(path = "{id}")
    public ResponseEntity<Client> update(@PathVariable Long id, @RequestBody Client client){
        return ResponseEntity.ok(clientService.update(id, client.getName(), client.getPhoneNumber()));
    }
}
