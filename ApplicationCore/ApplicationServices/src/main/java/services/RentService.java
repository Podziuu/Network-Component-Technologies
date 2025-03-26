package services;

import exception.*;
import infrastructure.ItemCommandPort;
import infrastructure.RentCommandPort;
import model.Rent;
import model.item.Item;
import model.user.Client;
import org.springframework.stereotype.Service;
import query.RentQueryPort;
import repo.MongoEntity;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentService {
    private final MongoEntity mongoEntity;
    private final RentCommandPort rentCommandPort;
    private final RentQueryPort rentQueryPort;
    private final ItemCommandPort itemCommandPort;

    public RentService(RentCommandPort rentCommandPort, RentQueryPort rentQueryPort, ItemCommandPort itemCommandPort) {
        this.rentCommandPort = rentCommandPort;
        this.rentQueryPort = rentQueryPort;
        this.itemCommandPort = itemCommandPort;
        mongoEntity = new MongoEntity();
    }

    public Rent rentItem(Rent rent) {
        Client client = rent.getClient();
        if (client == null) {
            throw new UserNotFoundException("User associated with rent id: " + rent.getId() + " not found");
        }

        Item item = rent.getItem();
        if (item == null) {
            throw new ItemNotFoundException("Item associated with rent ID: " + rent.getId()+ " not found");
        }

        if (!item.isAvailable()) {
            throw new ItemAlreadyRentedException("Item is already rented");
        }

        try (var session = mongoEntity.getMongoClient().startSession()) {
            session.startTransaction();

            item.setAvailable(false);
            itemCommandPort.updateItem(item);

            Rent createdRent = rentCommandPort.add(rent);

            session.commitTransaction();

            return createdRent;
        } catch (Exception e) {
            throw new RentOperationException("Error during rental operation: "  + e.getMessage(), e);
        }
    }

    public Rent getRentById(String rentId) {
        Rent rent = rentQueryPort.getById(rentId);
        if (rent == null) {
            throw new RentNotFoundException("Rent with ID: " + rentId + " not found");
        }

        return rent;
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

            item.setAvailable(true);
            itemCommandPort.updateItem(item);

            session.commitTransaction();
        } catch (Exception e) {
            throw new RentOperationException("Error during return operation: " + e.getMessage(), e);
        }
    }

    public List<Rent> getActiveRents() {
        List<Rent> rents = rentQueryPort.getActiveRents();
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rents;
    }

    public List<Rent> getInactiveRents() {
        List<Rent> rents = rentQueryPort.getInactiveRents();
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rents;
    }

    public List<Rent> getRentsByItem(String itemId) {
        List<Rent> rents = rentQueryPort.getByItemId(itemId);
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rents;
    }

    public List<Rent> getActiveRentsByItem(String itemId) {
        List<Rent> rents = rentQueryPort.findActiveRentsByItemId(itemId);
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rents;
    }

    public List<Rent> getInactiveRentsByItem(String itemId) {
        List<Rent> rents = rentQueryPort.findInactiveRentsByItemId(itemId);
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rents;
    }

    public List<Rent> getRentsByClient(String clientId) {
        List<Rent> rents = rentQueryPort.getByClientId(clientId);
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rents;
    }

    public List<Rent> getActiveRentsByClient(String clientId) {
        List<Rent> rents =  rentQueryPort.findActiveRentsByClientId(clientId);
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rents;
    }

    public List<Rent> getInactiveRentsByClient(String clientId) {
        List<Rent> rents =  rentQueryPort.findInactiveRentsByClientId(clientId);
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rents;
    }

    public boolean isItemRented(String itemId) {
        List<Rent> activeRents = rentQueryPort.findActiveRentsByItemId(itemId);
        if (activeRents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return !activeRents.isEmpty();
    }
}
