package pl.lodz.p.edu.rest.mapper;

import pl.lodz.p.edu.rest.dto.RentDTO;
import pl.lodz.p.edu.rest.model.Rent;

import java.util.List;
import java.util.stream.Collectors;

public class RentMapper {
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
}
