package ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions;

public class ClienteAlreadyExistException extends Throwable {
    public ClienteAlreadyExistException(String message) {
        super(message);
    }
}
