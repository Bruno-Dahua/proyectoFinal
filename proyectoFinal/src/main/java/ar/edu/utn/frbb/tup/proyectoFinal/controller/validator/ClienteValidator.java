package ar.edu.utn.frbb.tup.proyectoFinal.controller.validator;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.ClienteDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ClienteValidator {


    public void validate(ClienteDto clienteDto) {

        //valido que la fecha tenga formato LocalDate
        try {
            LocalDate.parse(clienteDto.getFechaNacimiento());
        } catch (Exception e) {
            throw new IllegalArgumentException("La FECHA DE NACIMIENTO ingresada no es valida.");
        }

        //valido campos String para que no se ingresen caracteres que no sean letras
        if (clienteDto.getNombre() == null || !clienteDto.getNombre().matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+")) {
            throw new IllegalArgumentException("El NOMBRE ingresado no es valido.");
        }

        if (clienteDto.getApellido() == null || !clienteDto.getApellido().matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+")){
            throw new IllegalArgumentException("El APELLIDO ingresado no es valido");
        }

        if (clienteDto.getBanco() == null || !clienteDto.getBanco().matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+")){
            throw new IllegalArgumentException("El BANCO ingresado no es valido.");
        }

        /*
        String dniString = String.valueOf(clienteDto.getDni()); // Convertir a String para usar matches
        if (!dniString.matches("[0-9]+") || dniString.length() != 8) {
            throw new IllegalArgumentException("El DNI ingresado no es válido.");
        }*/
    }
}

