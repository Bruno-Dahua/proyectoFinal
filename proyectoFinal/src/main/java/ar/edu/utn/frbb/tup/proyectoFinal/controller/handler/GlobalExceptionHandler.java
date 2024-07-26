package ar.edu.utn.frbb.tup.proyectoFinal.controller.handler;

import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ClienteDoesntExistException.class)
    public ResponseEntity<String> handleClienteDoesntExistException(ClienteDoesntExistException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(NotPosibleException.class)
    public ResponseEntity<String> handleNotPosibleException(NotPosibleException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(InputErrorException.class)
    public ResponseEntity<String> handleInputErrorException(InputErrorException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ClienteAlreadyExistException.class)
    public ResponseEntity<String> handleClienteAlreadyExistException(ClienteAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(CuentaAlreadyExistException.class)
    public ResponseEntity<String> handleCuentaAlreadyExistException(CuentaAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(TipoCuentaAlreadyExistException.class)
    public ResponseEntity<String> handleCuentaDoesntExistException(TipoCuentaAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(CuentaDoesntExistException.class)
    public ResponseEntity<String> handleCuentaDoesntExistException(CuentaDoesntExistException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}
