package ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions;

public class ClienteDoesntExistException extends Throwable{
    public ClienteDoesntExistException(String message) {
        super(message);
    }
}
