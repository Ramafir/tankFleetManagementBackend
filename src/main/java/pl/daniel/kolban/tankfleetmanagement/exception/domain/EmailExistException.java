package pl.daniel.kolban.tankfleetmanagement.exception.domain;

public class EmailExistException extends RuntimeException {
    public EmailExistException(String message) {
        super(message);
    }
}
