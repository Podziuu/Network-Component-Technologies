package pl.tks.restrent.controller;

import io.micrometer.core.annotation.Timed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tks.modelrent.client.Client;
import pl.tks.portsrent.ui.IClientPort;
import pl.tks.restrent.dto.ClientRentDTO;
import pl.tks.restrent.dto.CreateClientDTO;
import pl.tks.restrent.dto.UpdateClientDTO;
import pl.tks.restrent.mapper.ClientMapper;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final IClientPort clientPort;
    private final ClientMapper clientMapper;

    public ClientController(IClientPort clientPort, ClientMapper clientMapper) {
        this.clientPort = clientPort;
        this.clientMapper = clientMapper;
    }

    // Pobierz wszystkich klientów
    @Timed(value = "client.getAll", description = "Czas wykonania metody getAllClients")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ClientRentDTO> getAllClients(@RequestParam(required = false) String firstName) {
        List<Client> clients;
        if (firstName != null) {
            clients = clientPort.getClientsByFirstName(firstName);
        } else {
            clients = clientPort.getAllClients();
        }
        return clientMapper.toDTO(clients);
    }

    // Dodaj nowego klienta
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ClientRentDTO addClient(@RequestBody CreateClientDTO clientDTO) {
        Client client = clientMapper.convertToClient(clientDTO);
        Client createdClient = clientPort.addClient(client);
        return clientMapper.convertToDTO(createdClient);
    }

    // Pobierz klienta po ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ClientRentDTO> getClient(@PathVariable String id) {
        Client client = clientPort.getClientById(id);
        if (client == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clientMapper.convertToDTO(client));
    }

    // Zaktualizuj dane klienta
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> updateClient(@PathVariable String id, @RequestBody UpdateClientDTO clientDTO) {
        clientPort.updateClient(id, clientDTO.getFirstName(), clientDTO.getLastName());
        return ResponseEntity.noContent().build();
    }
}
