package pl.tks.ports.ui;

import pl.tks.model.client.Client;

import java.util.List;

public interface IClientPort {
    Client addClient(Client client);

    Client getClientById(String id);

    List<Client> getAllClients();

    List<Client> getClientsByFirstName(String firstName);

    void updateClient(String id, String firstName, String lastName);
}
