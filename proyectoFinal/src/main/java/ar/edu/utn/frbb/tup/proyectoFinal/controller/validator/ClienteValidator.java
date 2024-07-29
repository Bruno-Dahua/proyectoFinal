package ar.edu.utn.frbb.tup.proyectoFinal.controller.validator;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.InputErrorException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ClienteValidator {


    public void validate(ClienteDto clienteDto) throws InputErrorException {

        //valido que la fecha tenga formato LocalDate
        try {
            LocalDate.parse(clienteDto.getFechaNacimiento());
        } catch (Exception e) {
            throw new InputErrorException("La FECHA DE NACIMIENTO ingresada no es valida.");
        }

        //valido campos String para que no se ingresen caracteres que no sean letras
        if (clienteDto.getNombre() == null || !clienteDto.getNombre().matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+")) {
            throw new InputErrorException("El NOMBRE ingresado no es valido.");
        }

        if (clienteDto.getApellido() == null || !clienteDto.getApellido().matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+")){
            throw new InputErrorException("El APELLIDO ingresado no es valido");
        }

        if (clienteDto.getBanco() == null || !clienteDto.getBanco().matches("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]+")){
            throw new InputErrorException("El BANCO ingresado no es valido.");
        }

        if (!clienteDto.getDni().matches("\\d{8}")) {
            throw new InputErrorException("El DNI ingresado no es válido.");
        }

        if (!"PERSONA_FISICA".equalsIgnoreCase(clienteDto.getTipoPersona()) && !"PERSONA_JURIDICA".equalsIgnoreCase(clienteDto.getTipoPersona())) {
            throw new InputErrorException("El TIPO DE PERSONA no es valido.");
        }
    }
}

