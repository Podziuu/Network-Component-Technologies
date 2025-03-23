package infrastructure;

import model.Rent;

public interface RentCommandPort {
    Rent add(Rent rent);

    void update(Rent rent);
}
