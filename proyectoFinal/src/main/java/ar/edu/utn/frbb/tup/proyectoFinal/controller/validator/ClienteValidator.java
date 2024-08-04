package ar.edu.utn.frbb.tup.proyectoFinal.controller.validator;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.InputErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ClienteValidator {

    @Autowired
    private Validaciones validaciones;

    public void validate(ClienteDto clienteDto) throws InputErrorException {
        validaciones.validarDni(clienteDto.getDni());
        validaciones.validarString(clienteDto.getNombre(), "NOMBRE");
        validaciones.validarString(clienteDto.getApellido(), "APELLIDO");
        validaciones.validarString(clienteDto.getBanco(), "BANCO");
        validaciones.validarFechaNacimiento(clienteDto.getFechaNacimiento());
        validaciones.validarTipoPersona(clienteDto.getTipoPersona());
    }
}

