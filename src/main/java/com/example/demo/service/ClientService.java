package com.example.demo.service;

import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repository.Client;
import com.example.demo.repository.ClientRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client create(Client client) {
        return clientRepository.save(client);
    }


    public void delete(Long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if(optionalClient.isEmpty())
            throw new ResourceNotFoundException("Client with id: " + id + " doesn't exist.");

        clientRepository.deleteById(id);
    }

    @Transactional
    public Client update(Long id, String name, String phoneNumber) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if(optionalClient.isEmpty())
            throw new ResourceNotFoundException("Client with id: " + id + " doesn't exist.");

        Client client = optionalClient.get();

        if(name != null && !client.getName().equals(name))
            client.setName(name);

        if(phoneNumber != null && !client.getPhoneNumber().equals(phoneNumber))
            client.setPhoneNumber(phoneNumber);

        return client;
    }
}
