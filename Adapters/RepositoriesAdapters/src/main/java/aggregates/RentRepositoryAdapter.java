package aggregates;

import infrastructure.RentPort;
import mappers.RentMapper;
import model.Rent;
import org.springframework.stereotype.Component;
import repo.RentRepository;

import java.util.List;
@Component
public class RentRepositoryAdapter implements RentPort {
    private final RentRepository rentRepository;

    public RentRepositoryAdapter(RentRepository rentRepository) {
        this.rentRepository = rentRepository;
    }

    @Override
    public Rent add(Rent rent) {
        return RentMapper.toRent(rentRepository.addRent(RentMapper.toEnt(rent)));
    }

    @Override
    public void update(Rent rent) {
        rentRepository.updateRent(RentMapper.toEnt(rent));
    }

    @Override
    public Rent getById(String id) {
        return RentMapper.toRent(rentRepository.getRent(id));
    }

    @Override
    public List<Rent> getByItemId(String itemId) {
        return RentMapper.toRentList(rentRepository.findRentsByItemId(itemId));
    }

    @Override
    public List<Rent> getByClientId(String clientId) {
        return RentMapper.toRentList(rentRepository.findRentsByClientId(clientId));
    }

    @Override
    public List<Rent> findActiveRentsByItemId(String itemId) {
        return RentMapper.toRentList(rentRepository.findRentsByItemId(itemId));
    }

    @Override
    public List<Rent> getActiveRents() {
        return RentMapper.toRentList(rentRepository.findActiveRents());
    }

    @Override
    public List<Rent> getInactiveRents() {
        return RentMapper.toRentList(rentRepository.findInactiveRents());
    }

    @Override
    public List<Rent> findInactiveRentsByItemId(String itemdId) {
        return RentMapper.toRentList(rentRepository.findInactiveRentsByItemId(itemdId));
    }

    @Override
    public List<Rent> findActiveRentsByClientId(String clientId) {
        return RentMapper.toRentList(rentRepository.findActiveRentsByClientId(clientId));
    }

    @Override
    public List<Rent> findInactiveRentsByClientId(String clientId) {
        return RentMapper.toRentList(rentRepository.findInactiveRentsByClientId(clientId));
    }
}
