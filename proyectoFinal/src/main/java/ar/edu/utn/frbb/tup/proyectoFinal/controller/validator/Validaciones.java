package ar.edu.utn.frbb.tup.proyectoFinal.controller.validator;

import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoPersona;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.InputErrorException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

@Component
public class Validaciones {
    public void validarDni(long dni) throws InputErrorException {
        String dniString = String.valueOf(dni);

        if(dniString.length() != 8){
            throw new InputErrorException("El DNI ingresado no es valido");
        }

        if (!dniString.matches("\\d{7,8}")) {
            throw new InputErrorException("El DNI ingresado no es valido");
        }
    }

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void validarFechaNacimiento(String fechaNacimientoString){
        try{
            LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoString, DATE_FORMATTER);
            LocalDate fechaActual = LocalDate.now();

            if (fechaNacimiento.isAfter(fechaActual)) {
                throw new InputErrorException("La FECHA DE NACIMIENTO ingresada no es valida.");
            }


        }  catch (DateTimeParseException | InputErrorException e) {
            throw new IllegalArgumentException("La FECHA DE NACIMIENTO ingresada no es valida.");
        }
    }

    public void validarString(String string, String campo) throws InputErrorException {
        if(string == null){
            throw new InputErrorException("El campo " + campo + " ingresado no puede ser nulo");
        }

        if(!string.matches("[a-zA-Z]+")){
            throw new InputErrorException("El " + campo + " ingresado no es valido.");
        }
    }

    public void validarTipoPersona(String tipoPersona) throws InputErrorException {
        if (!"PERSONA_FISICA".equalsIgnoreCase(tipoPersona) && !"PERSONA_JURIDICA".equalsIgnoreCase(tipoPersona)) {
            throw new InputErrorException("El TIPO DE PERSONA ingresado no es valido.");
        }
    }

    public void validarTipoCuenta(String tipoCuenta) throws InputErrorException {
        if (!"CAJA_AHORRO".equalsIgnoreCase(tipoCuenta) && !"CUENTA_CORRIENTE".equalsIgnoreCase(tipoCuenta)) {
            throw new InputErrorException("El TIPO DE CUENTA ingresado no es valido.");
        }
    }

    public void validarMoneda(String moneda) throws InputErrorException {
        if (!"PESOS".equalsIgnoreCase(moneda) && !"DOLARES".equalsIgnoreCase(moneda)) {
            throw new InputErrorException("El TIPO DE MONEDA ingresado no es valido.");
        }
    }

    public void validarNumeroCuenta(long numeroCuenta){

        String nroCuentaString = String.valueOf(numeroCuenta);

        if(nroCuentaString.length() >= 8 || nroCuentaString.length() <= 1) {
            throw new IllegalArgumentException("El NUMERO DE CUENTA ingresado no es valido.");

        }

        if (!nroCuentaString.matches("\\d{19}")) {
            throw new IllegalArgumentException("El NUMERO DE CUENTA ingresado no es valido.");
        }
    }

    public void validarMonto(double monto) throws InputErrorException {
        if (monto <= 0) {
            throw new InputErrorException("El MONTO ingresado no es valido.");
        }

        try{
            Double.parseDouble(String.valueOf(monto));
            if(monto <= 0){
                throw new InputErrorException("El MONTO ingresado no es valido.");
            }
        }catch (NumberFormatException | InputErrorException e){
            throw new IllegalArgumentException("El MONTO ingresado no es valido.");
        }
    }
}
