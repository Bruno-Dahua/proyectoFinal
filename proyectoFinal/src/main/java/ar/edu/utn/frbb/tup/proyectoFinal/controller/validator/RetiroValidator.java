package ar.edu.utn.frbb.tup.proyectoFinal.controller.validator;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.DepositoDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.RetiroDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.InputErrorException;
import org.springframework.stereotype.Component;

@Component
public class RetiroValidator {
    public void validate(RetiroDto retiroDto) throws InputErrorException {
        if ((!retiroDto.getCuenta().matches("\\d{8}"))) {
            throw new InputErrorException("El NUMERO DE CUENTA ingresado no es válido.");
        }
        double monto;
        try {
            monto = Double.parseDouble(retiroDto.getMonto());
        } catch (Exception e) {
            throw new InputErrorException("El MONTO no es valido.");
        }

        if (Double.parseDouble(retiroDto.getMonto()) <= 0) {
            throw new InputErrorException("El MONTO no es valido.");
        }
        if (!"PESOS".equals(retiroDto.getMoneda().name()) && !"DOLARES".equals(retiroDto.getMoneda().name())) {
            throw new InputErrorException("El TIPO DE MONEDA no es valido.");
        }
    }
}
