package pl.daniel.kolban.tankfleetmanagement.exception.domain;

public class WrongArgumentException extends Exception {
    public WrongArgumentException(String message) {
        super(message);
    }
}
