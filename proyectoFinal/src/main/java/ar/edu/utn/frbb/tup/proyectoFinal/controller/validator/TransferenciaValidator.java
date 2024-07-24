package ar.edu.utn.frbb.tup.proyectoFinal.controller.validator;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.InputErrorException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import org.springframework.stereotype.Component;

@Component
public class TransferenciaValidator {
    public void validate(TransferenciaDto transferenciaDto) throws NotPosibleException, InputErrorException {
        if ((!transferenciaDto.getCuentaOrigen().matches("\\d{8}")) || (!transferenciaDto.getCuentaDestino().matches("\\d{8}"))) {
            throw new InputErrorException("El DNI ingresado no es válido.");
        }
        double monto;
        try {
            monto = Double.parseDouble(transferenciaDto.getMonto());
        } catch (Exception e) {
            throw new InputErrorException("El MONTO no es valido.");
        }

        if (Double.parseDouble(transferenciaDto.getMonto()) <= 0) {
            throw new InputErrorException("El MONTO no es valido.");
        }
        if (!"PESOS".equals(transferenciaDto.getMoneda().name()) && !"DOLARES".equals(transferenciaDto.getMoneda().name())) {
            throw new InputErrorException("El tipo de moneda no es correcto");
        }
    }
}
