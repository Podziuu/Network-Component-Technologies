package pl.tks.reposrent.mappers;

import pl.tks.modelrent.Rent;
import pl.tks.reposrent.entities.RentEnt;

import java.util.List;
import java.util.stream.Collectors;

public class RentMapper {
//    public static List<Rent>
    public static RentEnt toEnt(Rent rent) {
        return new RentEnt(
                rent.getBeginTime(),
                rent.getRentCost(),
                ClientMapper.toClientEnt(rent.getClient()),
                ItemMapper.toItemEnt(rent.getItem()));
    }

    public static Rent toRent(RentEnt rentEnt) {
        return new Rent(
                rentEnt.getBeginTime(),
                rentEnt.getRentCost(),
                ClientMapper.toClient(rentEnt.getClient()),
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
