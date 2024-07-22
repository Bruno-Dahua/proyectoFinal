package ar.edu.utn.frbb.tup.proyectoFinal.controller.validator;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.TransferenciaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import org.springframework.stereotype.Component;

@Component
public class TransferenciaValidator {
    public void validate(TransferenciaDto transferenciaDto) throws NotPosibleException {
        if ((!transferenciaDto.getCuentaOrigen().matches("\\d{8}")) || (!transferenciaDto.getCuentaDestino().matches("\\d{8}"))) {
            throw new IllegalArgumentException("El DNI ingresado no es válido.");
        }
        if (transferenciaDto.getMonto() <= 0) {
            throw new NotPosibleException("El monto debe ser mayor a 0.");
        }
        if (!"PESOS".equals(transferenciaDto.getMoneda().name()) && !"DOLARES".equals(transferenciaDto.getMoneda().name())) {
            throw new NotPosibleException("El tipo de moneda no es correcto");
        }
    }
}
