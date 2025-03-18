package pl.lodz.p.edu.rest.service;

import org.springframework.stereotype.Service;
import pl.lodz.p.edu.rest.dto.*;
import pl.lodz.p.edu.rest.exception.*;
import pl.lodz.p.edu.rest.mapper.ItemMapper;
import pl.lodz.p.edu.rest.mapper.RentMapper;
import pl.lodz.p.edu.rest.model.Rent;
import pl.lodz.p.edu.rest.model.item.Item;
import pl.lodz.p.edu.rest.model.user.Client;
import pl.lodz.p.edu.rest.model.user.User;
import pl.lodz.p.edu.rest.repository.ItemRepository;
import pl.lodz.p.edu.rest.repository.MongoEntity;
import pl.lodz.p.edu.rest.repository.RentRepository;
import pl.lodz.p.edu.rest.mapper.UserMapper;
import pl.lodz.p.edu.rest.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentService {
    private final RentRepository rentRepository;
    private final UserRepository userRepository;
    private final ItemService itemService;
    private final MongoEntity mongoEntity;
    private final UserMapper userMapper = new UserMapper();
    private final RentMapper rentMapper = new RentMapper();

    public RentService(RentRepository rentRepository, UserRepository userRepository, ItemService itemService, ItemRepository itemRepository) {
        this.rentRepository = rentRepository;
        this.userRepository = userRepository;
        this.itemService = itemService;
        mongoEntity = new MongoEntity();
    }

    public RentDTO rentItem(RentDTO rentDTO) {
        Client client = (Client) userRepository.findById(rentDTO.getClientId());
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

            itemService.setUnavailable(item.getId());

            Rent rent = new Rent(rentDTO.getBeginTime(), rentDTO.getRentCost(), client, item);
            rentRepository.addRent(rent);

            session.commitTransaction();

            return rentMapper.convertToDTO(rent);
        } catch (Exception e) {
            throw new RentOperationException("Error during rental operation: "  + e.getMessage(), e);
        }
    }

    public RentDTO getRentById(String rentId) {
        Rent rent = rentRepository.getRent(rentId);
        if (rent == null) {
            throw new RentNotFoundException("Rent with ID: " + rentId + " not found");
        }

        return rentMapper.convertToDTO(rent);
    }

    public void returnRent(String rentId) {
        try (var session = mongoEntity.getMongoClient().startSession()) {
            session.startTransaction();

            Rent rent = rentRepository.getRent(rentId);
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
            rentRepository.updateRent(rent);

            itemService.setAvailable(item.getId());

            session.commitTransaction();
        } catch (Exception e) {
            throw new RentOperationException("Error during return operation: " + e.getMessage(), e);
        }
    }

    public List<RentDTO> getActiveRents() {
        List<Rent> rents = rentRepository.findActiveRents();
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rentMapper.toDTO(rents);
    }

    public List<RentDTO> getInactiveRents() {
        List<Rent> rents = rentRepository.findInactiveRents();
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rentMapper.toDTO(rents);
    }

    public List<RentDTO> getRentsByItem(String itemId) {
        List<Rent> rents = rentRepository.findRentsByItemId(itemId);
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rentMapper.toDTO(rents);
    }

    public List<RentDTO> getActiveRentsByItem(String itemId) {
        List<Rent> rents = rentRepository.findActiveRentsByItemId(itemId);
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rentMapper.toDTO(rents);
    }

    public List<RentDTO> getInactiveRentsByItem(String itemId) {
        List<Rent> rents = rentRepository.findInactiveRentsByItemId(itemId);
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rentMapper.toDTO(rents);
    }

    public List<RentDTO> getRentsByClient(String clientId) {
        List<Rent> rents = rentRepository.findRentsByClientId(clientId);
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rentMapper.toDTO(rents);
    }

    public List<RentDTO> getActiveRentsByClient(String clientId) {
        List<Rent> rents =  rentRepository.findActiveRentsByClientId(clientId);
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rentMapper.toDTO(rents);
    }

    public List<RentDTO> getInactiveRentsByClient(String clientId) {
        List<Rent> rents =  rentRepository.findInactiveRentsByClientId(clientId);
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rentMapper.toDTO(rents);
    }

    public boolean isItemRented(String itemId) {
        List<Rent> activeRents = rentRepository.findActiveRentsByItemId(itemId);
        if (activeRents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return !activeRents.isEmpty();
    }
}
