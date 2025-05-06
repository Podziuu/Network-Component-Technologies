package pl.tks.portsrent.ui;

import pl.tks.modelrent.Rent;

import java.util.List;

public interface IRentPort {
    Rent add(Rent rent);

    void returnRent(String rentId);

    Rent getById(String id);

    List<Rent> getByItemId(String itemId);

    List<Rent> getByClientId(String clientId);

    List<Rent> findActiveRentsByItemId(String itemId);

    List<Rent> getActiveRents();

    List<Rent> getInactiveRents();

    List<Rent> findInactiveRentsByItemId(String itemdId);

    List<Rent> findActiveRentsByClientId(String clientId);

    List<Rent> findInactiveRentsByClientId(String clientId);
}
