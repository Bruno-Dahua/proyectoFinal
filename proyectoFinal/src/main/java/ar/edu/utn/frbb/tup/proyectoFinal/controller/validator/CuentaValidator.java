package ar.edu.utn.frbb.tup.proyectoFinal.controller.validator;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.CuentaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.ClienteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CuentaValidator {
    @Autowired
    private ClienteDao clienteDao;

    public void validate(CuentaDto cuentaDto, long dni) {
        if (!"PESOS".equals(cuentaDto.getMoneda().name()) && !"DOLARES".equals(cuentaDto.getMoneda().name())) {
            throw new IllegalArgumentException("El tipo de moneda no es correcto");
        }
        if (!"CUENTA_CORRIENTE".equals(cuentaDto.getTipoCuenta().name()) && !"CAJA_AHORRO".equals(cuentaDto.getTipoCuenta().name())) {
            throw new IllegalArgumentException("El tipo de cuenta no es correcto");
        }
        if (clienteDao.find(dni, true) == null) {
            throw new IllegalArgumentException("Cliente no existe");
        }
    }
}
