package ar.edu.utn.frbb.tup.proyectoFinal.service;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.RespuestaHistorialDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Transaccion;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HistorialService {
    @Autowired
    private CuentaDao cuentaDao;

    public RespuestaHistorialDto obtenerHistorial(long cuentaId) throws NotPosibleException {
        Cuenta cuenta = cuentaDao.findByNumeroCuenta(cuentaId);
        if (cuenta == null) {
            throw new NotPosibleException("No existe una cuenta con el numero de cuenta: " + cuentaId);
        }

        Set<Transaccion> transacciones = cuenta.getHistorialTransacciones().stream()
                .map(movimiento -> new Transaccion(
                        movimiento.getNumeroMovimiento(),
                        movimiento.getFecha(),
                        movimiento.getMonto(),
                        movimiento.getTipo(),
                        movimiento.getDescripcion()))
                .collect(Collectors.toSet());

        RespuestaHistorialDto respuestaHistorial = new RespuestaHistorialDto();
        respuestaHistorial.setNumeroCuenta(cuentaId);
        respuestaHistorial.setHistorialTransacciones(transacciones);

        return respuestaHistorial;
    }
}
