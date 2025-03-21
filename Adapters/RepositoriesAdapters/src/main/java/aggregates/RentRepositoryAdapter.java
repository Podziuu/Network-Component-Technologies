package aggregates;

import command.RentCommandPort;
import model.Rent;
import query.RentQueryPort;

import java.util.List;

public class RentRepositoryAdapter implements RentQueryPort, RentCommandPort {
    @Override
    public Rent add(Rent rent) {
        return null;
    }

    @Override
    public void remove(Rent rent) {

    }

    @Override
    public Rent update(Rent rent) {
        return null;
    }

    @Override
    public boolean removeById(String rentId) {
        return false;
    }


    @Override
    public Rent getById(String id) {
        return null;
    }

    @Override
    public List<Rent> getAll() {
        return List.of();
    }

    @Override
    public List<Rent> getByItemId(String itemId) {
        return List.of();
    }

    @Override
    public List<Rent> getByClientId(String clientId) {
        return List.of();
    }

    @Override
    public List<Rent> findByItemAndStatus(String itemId, boolean isRented) {
        return List.of();
    }

    @Override
    public List<Rent> findByClientAndStatus(String clientId, boolean isRented) {
        return List.of();
    }

    @Override
    public List<Rent> findActiveRentsByItemId(String itemId) {
        return List.of();
    }

    @Override
    public List<Rent> getActiveRents() {
        return List.of();
    }

    @Override
    public List<Rent> getInactiveRents() {
        return List.of();
    }

    @Override
    public List<Rent> findInactiveRentsByItemId(String itemdId) {
        return List.of();
    }

    @Override
    public List<Rent> findActiveRentsByClientId(String clientId) {
        return List.of();
    }

    @Override
    public List<Rent> findInactiveRentsByClientId(String clientId) {
        return List.of();
    }
}
