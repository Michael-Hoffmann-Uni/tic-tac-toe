package jpp.games.networking.server.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ServerExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleException(IllegalStateException e) {
        String message = "An IllegalStateException with the following message occured:\n" + e.toString();
        ResponseEntity<String> response = new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
        return response;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException e) {
        String message = "An IllegalArgumentException with the following message occured:\n" + e.toString();
        ResponseEntity<String> response = new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
        return response;
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity<String> handleException(IllegalAccessException e) {
        String message = "An IllegalAccessException with the following message occured:\n" + e.toString();
        ResponseEntity<String> response = new ResponseEntity<String>(message, HttpStatus.BAD_REQUEST);
        return response;
    }
}
