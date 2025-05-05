package pl.tks.ports.infrastructure;

import pl.tks.model.client.Client;

import java.util.List;

public interface ClientPort {
    Client addClient(Client client);

    Client getById(String id);

    List<Client> getAll();

    List<Client> findByFirstName(String firstName);

    void updateClient(String id, String firstName, String lastName);
}
