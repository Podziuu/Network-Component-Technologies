package pl.tks.repos.mappers;

import pl.tks.model.Rent;
import pl.tks.repos.entities.RentEnt;

import java.util.List;
import java.util.stream.Collectors;

public class RentMapper {
//    public static List<Rent>
    public static RentEnt toEnt(Rent rent) {
        return new RentEnt(
                rent.getBeginTime(),
                rent.getRentCost(),
                UserMapper.toClientEnt(rent.getClient()),
                ItemMapper.toItemEnt(rent.getItem()));
    }

    public static Rent toRent(RentEnt rentEnt) {
        return new Rent(
                rentEnt.getBeginTime(),
                rentEnt.getRentCost(),
                UserMapper.toClient(rentEnt.getClient()),
                ItemMapper.toItem(rentEnt.getItem())
        );
    }

    public static List<RentEnt> toEntList(List<Rent> rents) {
        return rents.stream().map(RentMapper::toEnt).collect(Collectors.toList());
    }

    public static List<Rent> toRentList(List<RentEnt> rentEnts) {
        return rentEnts.stream().map(RentMapper::toRent).collect(Collectors.toList());
    }
}
