package pl.daniel.kolban.tankfleetmanagement.exception.domain;

public class UsernameExistException extends Exception {
    public UsernameExistException(String message) {
        super(message);
    }
}
