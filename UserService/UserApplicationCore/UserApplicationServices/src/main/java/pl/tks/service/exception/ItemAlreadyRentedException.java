package pl.tks.service.exception;

public class ItemAlreadyRentedException extends RuntimeException {
    public ItemAlreadyRentedException(String message) {super(message);}
}
