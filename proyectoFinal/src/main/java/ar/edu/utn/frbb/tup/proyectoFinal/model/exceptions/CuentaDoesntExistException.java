package ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions;

public class CuentaDoesntExistException extends Throwable{
    public CuentaDoesntExistException(String message) {
        super(message);
    }
}
