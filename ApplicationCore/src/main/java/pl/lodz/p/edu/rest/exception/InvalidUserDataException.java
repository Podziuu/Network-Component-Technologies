package pl.lodz.p.edu.rest.exception;

public class InvalidUserDataException extends RuntimeException {
    public InvalidUserDataException(String message) {
        super(message);
    }
}
