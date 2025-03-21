package query;

import model.Rent;

import java.util.List;
import java.util.Optional;

public interface RentQueryPort {
    Optional<Rent> getById(String id);

    List<Rent> getAll();

    List<Rent> getByItemId(String itemId);

    List<Rent> getByClientId(String clientId);

    List<Rent> findByItemAndStatus(String itemId, boolean isRented);

    List<Rent> findByClientAndStatus(String clientId, boolean isRented);

}
