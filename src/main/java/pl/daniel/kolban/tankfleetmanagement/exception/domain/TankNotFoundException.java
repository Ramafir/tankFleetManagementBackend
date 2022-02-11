package pl.daniel.kolban.tankfleetmanagement.exception.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        value = HttpStatus.BAD_REQUEST,
        reason = "Cannot find tank with this ID")
public class TankNotFoundException extends RuntimeException { }
