package pl.tks.servicerent.exception;

public class ItemAlreadyRentedException extends RuntimeException {
    public ItemAlreadyRentedException(String message) {super(message);}
}
