package pl.tks.restrent.mapper;

import org.springframework.stereotype.Component;
import pl.tks.modelrent.Rent;
import pl.tks.modelrent.client.Client;
import pl.tks.modelrent.item.Item;
import pl.tks.portsrent.infrastructure.ClientPort;
import pl.tks.portsrent.infrastructure.ItemPort;
import pl.tks.restrent.dto.RentDTO;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RentMapper {

    private final ClientPort clientPort;
    private final ItemPort itemPort;

    public RentMapper(ClientPort clientPort, ItemPort itemPort) {
        this.clientPort = clientPort;
        this.itemPort = itemPort;
    }

    public List<RentDTO> toDTO(List<Rent> rents) {
        return rents.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RentDTO convertToDTO(Rent rent) {
        return new RentDTO(
                rent.getId().toString(),
                rent.getBeginTime(),
                rent.getEndTime(),
                rent.getRentCost(),
                rent.isArchive(),
                rent.getClient().getId().toString(),
                rent.getItem().getId().toString()
        );
    }

    public Rent toDomain(RentDTO dto) {
        Client user = clientPort.getById(dto.getClientId());
        Item item = itemPort.getItemById(dto.getItemId());

        Rent rent = new Rent();
        rent.setId(dto.getId());
        rent.setBeginTime(dto.getBeginTime());
        rent.setEndTime(dto.getEndTime());
        rent.setRentCost(dto.getRentCost());
        rent.setArchive(dto.isArchive());
        rent.setClient(user);
        rent.setItem(item);
        return rent;
    }
}
