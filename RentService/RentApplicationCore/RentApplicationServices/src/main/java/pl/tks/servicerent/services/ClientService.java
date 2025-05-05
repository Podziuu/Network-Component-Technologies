package pl.tks.service.services;


import org.springframework.stereotype.Service;
import pl.tks.model.client.Client;
import pl.tks.ports.infrastructure.ClientPort;
import pl.tks.ports.ui.IClientPort;

import java.util.List;

@Service
public class ClientService implements IClientPort {
    private final ClientPort clientPort;

    public ClientService(ClientPort clientPort) {
        this.clientPort = clientPort;
    }

    public Client addClient(Client client) {
        return clientPort.addClient(client);
    }

    public Client getClientById(String id) {
        Client client = clientPort.getById(id);
        return client;
    }

    public List<Client> getAllClients() {
        List<Client> clients = clientPort.getAll();
        return clients;
    }

    public List<Client> getClientsByFirstName(String firstName) {
        List<Client> clients = clientPort.findByFirstName(firstName);
        return clients;
    }

    public void updateClient(String id, String firstName, String lastName) {
        clientPort.updateClient(id, firstName, lastName);
    }
}
