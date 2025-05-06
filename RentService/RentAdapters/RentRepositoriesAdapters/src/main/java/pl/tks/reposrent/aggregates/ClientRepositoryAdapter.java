package pl.tks.reposrent.aggregates;

import org.springframework.stereotype.Component;
import pl.tks.modelrent.client.Client;
import pl.tks.portsrent.infrastructure.ClientPort;
import pl.tks.reposrent.entities.ClientEnt;
import pl.tks.reposrent.mappers.ClientMapper;
import pl.tks.reposrent.repo.ClientRepository;

import java.util.List;

@Component
public class ClientRepositoryAdapter implements ClientPort {
    private final ClientRepository clientRepository;

    public ClientRepositoryAdapter(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client addClient(Client client) {
        return ClientMapper.toClient(clientRepository.addClient(ClientMapper.toClientEnt(client)));
    }

    @Override
    public void updateClient(String id, String firstName, String lastName) {
        ClientEnt client = clientRepository.getClient(id); // Pobranie klienta po ID
        if (client != null) {
            client.setFirstName(firstName); // Zmiana imienia
            client.setLastName(lastName); // Zmiana nazwiska
            clientRepository.updateClient(client); // Zaktualizowanie danych w repozytorium
        }
    }

    @Override
    public Client getById(String id) {
        return ClientMapper.toClient(clientRepository.getClient(id));
    }

    @Override
    public List<Client> getAll() {
        return ClientMapper.toClientList(clientRepository.getAllClients());
    }



    @Override
    public List<Client> findByFirstName(String firstName) {
        return ClientMapper.toClientList(clientRepository.findClientsByFirstName(firstName));
    }
}
