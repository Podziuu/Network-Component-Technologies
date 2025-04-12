package pl.lodz.p.edu.restadapters.mapper;

import org.springframework.stereotype.Component;
import pl.lodz.p.edu.restadapters.dto.RentDTO;
import infrastructure.ItemPort;
import infrastructure.UserPort;
import model.Rent;
import model.item.Item;
import model.user.Client;
import model.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RentMapper {

    private final UserPort userPort;
    private final ItemPort itemPort;

    public RentMapper(UserPort userPort, ItemPort itemPort) {
        this.userPort = userPort;
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
        User user = userPort.getById(dto.getClientId());
        Item item = itemPort.getItemById(dto.getItemId());

        Rent rent = new Rent();
        rent.setId(dto.getId());
        rent.setBeginTime(dto.getBeginTime());
        rent.setEndTime(dto.getEndTime());
        rent.setRentCost(dto.getRentCost());
        rent.setArchive(dto.isArchive());
        rent.setClient((Client) user);
        rent.setItem(item);
        return rent;
    }
}
