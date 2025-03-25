package services;

import dto.ItemDTO;
import dto.RentDTO;
import exception.*;
import infrastructure.RentCommandPort;
import infrastructure.UserCommandPort;
import mapper.ItemMapper;
import mapper.RentMapper;
import model.Rent;
import model.item.Item;
import model.user.Client;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import query.RentQueryPort;
import query.UserQueryPort;
import repo.MongoEntity;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentService {
    private final ItemService itemService;
    private final MongoEntity mongoEntity;
    private final RentCommandPort rentCommandPort;
    private final RentQueryPort rentQueryPort;
    private final UserCommandPort userCommandPort;
    private final UserQueryPort userQueryPort;
    private final RentMapper rentMapper = new RentMapper();

    public RentService(RentCommandPort rentCommandPort, RentQueryPort rentQueryPort, ItemService itemService, UserCommandPort userCommandPort, UserQueryPort userQueryPort) {
        this.rentCommandPort = rentCommandPort;
        this.rentQueryPort = rentQueryPort;
        this.userCommandPort = userCommandPort;
        this.userQueryPort = userQueryPort;
        this.itemService = itemService;
        mongoEntity = new MongoEntity();
    }

    public RentDTO rentItem(RentDTO rentDTO) {
        Client client = (Client) userQueryPort.getById(rentDTO.getClientId());
        if (client == null) {
            throw new UserNotFoundException("User with id " + rentDTO.getItemId() + " not found");
        }

        ItemDTO itemDTO = itemService.getItemById(rentDTO.getItemId());
        if (itemDTO == null) {
            throw new ItemNotFoundException("Item with ID: " + rentDTO.getItemId() + " not found");
        }

        Item item = ItemMapper.toItem(itemDTO);

        if (!item.isAvailable()) {
            throw new ItemAlreadyRentedException("Item is already rented");
        }

        try (var session = mongoEntity.getMongoClient().startSession()) {
            session.startTransaction();

            itemService.setUnavailable(new ObjectId(item.getId()));

            Rent rent = new Rent(rentDTO.getBeginTime(), rentDTO.getRentCost(), client, item);
            Rent createdRent = rentCommandPort.add(rent);

            session.commitTransaction();

            return rentMapper.convertToDTO(createdRent);
        } catch (Exception e) {
            throw new RentOperationException("Error during rental operation: "  + e.getMessage(), e);
        }
    }

    public RentDTO getRentById(String rentId) {
        Rent rent = rentQueryPort.getById(rentId);
        if (rent == null) {
            throw new RentNotFoundException("Rent with ID: " + rentId + " not found");
        }

        return rentMapper.convertToDTO(rent);
    }

    public void returnRent(String rentId) {
        try (var session = mongoEntity.getMongoClient().startSession()) {
            session.startTransaction();

            Rent rent = rentQueryPort.getById(rentId);
            if (rent == null) {
                throw new RentNotFoundException("Rent with ID: " + rentId + " not found");
            }

            Item item = rent.getItem();
            if (item == null) {
                throw new ItemNotFoundException("Item associated with rent ID: " + rentId + " not found");
            }

            LocalDateTime end = LocalDateTime.now();

            rent.setEndTime(end);
            rent.setArchive(true);
            rentCommandPort.update(rent);

            itemService.setAvailable(new ObjectId(item.getId()));

            session.commitTransaction();
        } catch (Exception e) {
            throw new RentOperationException("Error during return operation: " + e.getMessage(), e);
        }
    }

    public List<RentDTO> getActiveRents() {
        List<Rent> rents = rentQueryPort.getActiveRents();
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rentMapper.toDTO(rents);
    }

    public List<RentDTO> getInactiveRents() {
        List<Rent> rents = rentQueryPort.getInactiveRents();
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rentMapper.toDTO(rents);
    }

    public List<RentDTO> getRentsByItem(String itemId) {
        List<Rent> rents = rentQueryPort.getByItemId(itemId);
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rentMapper.toDTO(rents);
    }

    public List<RentDTO> getActiveRentsByItem(String itemId) {
        List<Rent> rents = rentQueryPort.findActiveRentsByItemId(itemId);
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rentMapper.toDTO(rents);
    }

    public List<RentDTO> getInactiveRentsByItem(String itemId) {
        List<Rent> rents = rentQueryPort.findInactiveRentsByItemId(itemId);
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rentMapper.toDTO(rents);
    }

    public List<RentDTO> getRentsByClient(String clientId) {
        List<Rent> rents = rentQueryPort.getByClientId(clientId);
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rentMapper.toDTO(rents);
    }

    public List<RentDTO> getActiveRentsByClient(String clientId) {
        List<Rent> rents =  rentQueryPort.findActiveRentsByClientId(clientId);
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rentMapper.toDTO(rents);
    }

    public List<RentDTO> getInactiveRentsByClient(String clientId) {
        List<Rent> rents =  rentQueryPort.findInactiveRentsByClientId(clientId);
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rentMapper.toDTO(rents);
    }

    public boolean isItemRented(String itemId) {
        List<Rent> activeRents = rentQueryPort.findActiveRentsByItemId(itemId);
        if (activeRents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return !activeRents.isEmpty();
    }
}
