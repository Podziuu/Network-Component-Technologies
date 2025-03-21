package query;

import model.Rent;

import java.util.List;
import java.util.Optional;

public interface RentQueryPort {
    Rent getById(String id);

    List<Rent> getAll();

    List<Rent> getByItemId(String itemId);

    List<Rent> getByClientId(String clientId);

    List<Rent> findByItemAndStatus(String itemId, boolean isRented);

    List<Rent> findByClientAndStatus(String clientId, boolean isRented);

    List<Rent> findActiveRentsByItemId(String itemId);

    List<Rent> getActiveRents();

    List<Rent> getInactiveRents();

    List<Rent> findInactiveRentsByItemId(String itemdId);

    List<Rent> findActiveRentsByClientId(String clientId);

    List<Rent> findInactiveRentsByClientId(String clientId);
}
