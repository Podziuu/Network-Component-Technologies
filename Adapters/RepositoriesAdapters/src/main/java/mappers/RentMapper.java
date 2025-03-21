package mappers;

import entities.RentEnt;
import model.Rent;
import model.user.Client;

public class RentMapper {

    public static Rent toModel(RentEnt rentEnt) {
        Rent rent = new Rent(
                rentEnt.getBeginTime(),
                rentEnt.getRentCost(),
                (Client) UserMapper.toModel(rentEnt.getClient()),
                ItemMapper.toModel(rentEnt.getItem())
        );

        rent.setId(rentEnt.getId().toString());
        rent.setEndTime(rentEnt.getEndTime());
        rent.setArchive(rentEnt.isArchive());

        return rent;
    }
}