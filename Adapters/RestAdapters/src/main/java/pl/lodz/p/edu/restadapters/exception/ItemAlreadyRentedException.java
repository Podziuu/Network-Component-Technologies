package pl.lodz.p.edu.restadapters.exception;

public class ItemAlreadyRentedException extends RuntimeException {
    public ItemAlreadyRentedException(String message) {super(message);}
}
