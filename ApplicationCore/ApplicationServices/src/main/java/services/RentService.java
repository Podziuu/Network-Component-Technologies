package services;

import exception.*;
import infrastructure.ItemPort;
import infrastructure.RentPort;
import model.Rent;
import model.item.Item;
import model.user.Client;
import org.springframework.stereotype.Service;
import repo.MongoEntity;
import ui.IRentPort;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RentService implements IRentPort {
    private final MongoEntity mongoEntity;
    private final RentPort rentPort;
    private final ItemPort itemPort;

    public RentService(RentPort rentPort, ItemPort itemPort) {
        this.rentPort = rentPort;
        this.itemPort = itemPort;
        mongoEntity = new MongoEntity();
    }

    //TODO zrobic dobrze te transakcje
    @Override
    public Rent add(Rent rent) {
        Client client = rent.getClient();
        if (client == null) {
            throw new UserNotFoundException("User associated with rent ID: " + rent.getId() + " not found");
        }

        Item item = rent.getItem();
        if (item == null) {
            throw new ItemNotFoundException("Item associated with rent ID: " + rent.getId() + " not found");
        }

        if (!item.isAvailable()) {
            throw new ItemAlreadyRentedException("Item is already rented");
        }

        try (var session = mongoEntity.getMongoClient().startSession()) {
            session.startTransaction();

            item.setAvailable(false);
            itemPort.updateItem(item);

            Rent createdRent = rentPort.add(rent);

            session.commitTransaction();

            return createdRent;
        } catch (Exception e) {
            throw new RentOperationException("Error during rental operation: " + e.getMessage(), e);
        }
    }

    @Override
    public Rent getById(String rentId) {
        Rent rent = rentPort.getById(rentId);
        if (rent == null) {
            throw new RentNotFoundException("Rent with ID: " + rentId + " not found");
        }

        return rent;
    }

    public void returnRent(String rentId) {
        try (var session = mongoEntity.getMongoClient().startSession()) {
            session.startTransaction();

            Rent rent = rentPort.getById(rentId);
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
            rentPort.update(rent);

            item.setAvailable(true);
            itemPort.updateItem(item);

            session.commitTransaction();
        } catch (Exception e) {
            throw new RentOperationException("Error during return operation: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Rent> getActiveRents() {
        List<Rent> rents = rentPort.getActiveRents();
        if (rents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return rents;
    }

    @Override
    public List<Rent> getInactiveRents() {
        List<Rent> rents = rentPort.getInactiveRents();
        if (rents == null) {
            throw new RentNotFoundException("No inactive rents found");
        }
        return rents;
    }

    @Override
    public List<Rent> getByItemId(String itemId) {
        List<Rent> rents = rentPort.getByItemId(itemId);
        if (rents == null) {
            throw new RentNotFoundException("No rents found for item ID: " + itemId);
        }
        return rents;
    }

    @Override
    public List<Rent> findActiveRentsByItemId(String itemId) {
        List<Rent> rents = rentPort.findActiveRentsByItemId(itemId);
        if (rents == null) {
            throw new RentNotFoundException("No active rents found for item ID: " + itemId);
        }
        return rents;
    }

    @Override
    public List<Rent> findInactiveRentsByItemId(String itemId) {
        List<Rent> rents = rentPort.findInactiveRentsByItemId(itemId);
        if (rents == null) {
            throw new RentNotFoundException("No inactive rents found for item ID: " + itemId);
        }
        return rents;
    }

    @Override
    public List<Rent> getByClientId(String clientId) {
        List<Rent> rents = rentPort.getByClientId(clientId);
        if (rents == null) {
            throw new RentNotFoundException("No rents found for client ID: " + clientId);
        }
        return rents;
    }

    @Override
    public List<Rent> findActiveRentsByClientId(String clientId) {
        List<Rent> rents = rentPort.findActiveRentsByClientId(clientId);
        if (rents == null) {
            throw new RentNotFoundException("No active rents found for client ID: " + clientId);
        }
        return rents;
    }

    @Override
    public List<Rent> findInactiveRentsByClientId(String clientId) {
        List<Rent> rents = rentPort.findInactiveRentsByClientId(clientId);
        if (rents == null) {
            throw new RentNotFoundException("No inactive rents found for client ID: " + clientId);
        }
        return rents;
    }

    public boolean isItemRented(String itemId) {
        List<Rent> activeRents = rentPort.findActiveRentsByItemId(itemId);
        if (activeRents == null) {
            throw new RentNotFoundException("No active rents found");
        }
        return !activeRents.isEmpty();
    }
}