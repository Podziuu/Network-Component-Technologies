package pl.tks.rest.mapper;

import org.springframework.stereotype.Component;
import pl.tks.model.client.Client;
import pl.tks.rest.dto.ClientRentDTO;
import pl.tks.rest.dto.CreateClientDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClientMapper {

    // Mapa dla listy klientów
    public List<ClientRentDTO> toDTO(List<Client> clients) {
        return clients.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Mapa pojedynczego klienta
    public ClientRentDTO convertToDTO(Client client) {
        return new ClientRentDTO(
                client.getId(),  // Przypisanie ID klienta
                client.getFirstName(),
                client.getLastName(),
                client.getMaxArticles(),
                client.getDiscount()
        );
    }

    // Mapowanie CreateClientDTO na Client
    public Client convertToClient(CreateClientDTO clientDTO) {
        return new Client(
                clientDTO.getId(),
                clientDTO.getFirstName(),
                clientDTO.getLastName(),
                clientDTO.getMaxArticles(),
                clientDTO.getDiscount()
        );
    }
}
