package pl.tks.ports.infrastructure;

import pl.tks.model.Rent;

import java.util.List;
public interface RentPort {
    Rent add(Rent rent);

    void update(Rent rent);
    Rent getById(String id);

    List<Rent> getByItemId(String itemId);

    List<Rent> getByClientId(String clientId);

    List<Rent> findActiveRentsByItemId(String itemId);

    List<Rent> getActiveRents();

    List<Rent> getInactiveRents();

    List<Rent> findInactiveRentsByItemId(String itemId);

    List<Rent> findActiveRentsByClientId(String clientId);

    List<Rent> findInactiveRentsByClientId(String clientId);
}
