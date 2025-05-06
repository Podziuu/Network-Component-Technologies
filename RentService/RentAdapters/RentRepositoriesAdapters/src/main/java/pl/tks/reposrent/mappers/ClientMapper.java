package pl.tks.reposrent.mappers;

import pl.tks.modelrent.client.Client;
import pl.tks.reposrent.entities.ClientEnt;

import java.util.List;
import java.util.stream.Collectors;

public class ClientMapper {

    // Mapowanie z Client do ClientEnt (dla MongoDB)
    public static ClientEnt toClientEnt(Client client) {
        return new ClientEnt(
                client.getFirstName(),
                client.getLastName(),
                client.getMaxArticles(),
                client.getDiscount()
        );
    }

    // Mapowanie z ClientEnt do Client
    public static Client toClient(ClientEnt clientEnt) {
        return new Client(
                clientEnt.getId() != null ? clientEnt.getId().toString() : null, // Przekształcenie ObjectId na String
                clientEnt.getFirstName(),
                clientEnt.getLastName(),
                clientEnt.getMaxArticles(),
                clientEnt.getDiscount()
        );
    }

    // Mapowanie listy ClientEnt na listę Client
    public static List<Client> toClientList(List<ClientEnt> clientEnts) {
        return clientEnts.stream()
                .map(ClientMapper::toClient)
                .collect(Collectors.toList());
    }

    // Mapowanie listy Client na listę ClientEnt
    public static List<ClientEnt> toClientEntList(List<Client> clients) {
        return clients.stream()
                .map(ClientMapper::toClientEnt)
                .collect(Collectors.toList());
    }
}