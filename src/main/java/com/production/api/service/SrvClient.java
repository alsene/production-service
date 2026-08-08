package com.production.api.service;

import com.production.api.model.Client;
import com.production.api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SrvClient {
    private final ClientRepository ClientRepository;
    
    public Mono<Client> getClient(){
        Client Client = new Client();
        Client.setNom("Client Test2");
        log.info("Returning Client: {}", Client);
        return Mono.just(Client);
    }

    /**
     * Save a product to the database
     */
    public Mono<Client> saveClient(Client Client) {
        return Mono.fromCallable(() -> {
            log.info("Saving Client to database: {}", Client);
            Client saved = ClientRepository.save(Client);
            log.info("Client saved with id: {}", saved.getId());
            return saved;
        });
    }

    public Mono<Client> modifierClient(Client client) {
        return Mono.fromCallable(() -> {
            if (client.getId() == null) {
                throw new IllegalArgumentException("L'id du client est requis pour la modification");
            }

            Client existingClient = ClientRepository.findById(client.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + client.getId()));

            existingClient.setNom(client.getNom());
            existingClient.setEmail(client.getEmail());
            existingClient.setTelephone(client.getTelephone());
            existingClient.setNumero(client.getNumero());
            existingClient.setRue(client.getRue());
            existingClient.setVille(client.getVille());
            existingClient.setPays(client.getPays());
            existingClient.setCodePostal(client.getCodePostal());

            return ClientRepository.save(existingClient);
        });
    }

    public Mono<Void> supprimerClient(Long id) {
        return Mono.fromCallable(() -> {
            Client existingClient = ClientRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + id));
            ClientRepository.deleteById(existingClient.getId());
            return null;
        });
    }

    /**
     * Get product by ID from database
     */
    public Mono<Client>  getClientById(Long id) {
        return Mono.fromCallable(() -> {
            log.info("Fetching Client with id: {}", id);
            return ClientRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
        });
    }

    /**
     * Get all products from database
     */
    public Mono<List<Client>> getAllClients() {
        return Mono.fromCallable(() -> {
            log.info("Fetching all Clients from database");
            return ClientRepository.findAll();
        });
    }
}