package ar.edu.utn.frbb.tup.proyectoFinal.service;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.DepositoDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.RespuestaTransaccionDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.RetiroDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.*;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.CuentaDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.InputErrorException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.ClienteDao;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RetiroService {
    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private CuentaService cuentaService;
    public RespuestaTransaccionDto realizarRetiro(RetiroDto retiroDto) throws NotPosibleException, ClienteDoesntExistException, InputErrorException, CuentaDoesntExistException {
        Cuenta cuenta = cuentaDao.findByNumeroCuenta(Long.parseLong(retiroDto.getCuenta()));

        if (cuenta == null) {
            throw new CuentaDoesntExistException("La cuenta " + retiroDto.getCuenta() + " no existe.");
        }
        if (!cuenta.getMoneda().equals(retiroDto.getMoneda())) {
            throw new NotPosibleException("Las monedas de las cuentas no coinciden");
        }

        RespuestaTransaccionDto respuestaTransaccionDto = new RespuestaTransaccionDto();

        return realizarRetiroYActualizarBalance(cuenta, retiroDto, respuestaTransaccionDto);
    }

    private RespuestaTransaccionDto realizarRetiroYActualizarBalance(Cuenta cuenta, RetiroDto retiroDto, RespuestaTransaccionDto respuestaRetiroDto) throws NotPosibleException {
        Retiro retiro = toRetiro(retiroDto);

        double monto = Double.parseDouble(retiroDto.getMonto());
        double comision = calcularComision(retiroDto);
        if (cuenta.getBalance() >= monto) {

            cuentaService.actualizarBalanceRetiro(cuenta, monto, comision);

            save(cuenta, retiro, "Retiro de la cuenta " + cuenta.getNumeroCuenta() + ".");

            respuestaRetiroDto.setEstado("EXITOSA");
            respuestaRetiroDto.setMensaje("Se realizó el deposito exitosamente. Número de transaccion: " + retiro.getNumeroTransaccion() + ". Realizado el " + retiro.getFecha() + ". Saldo actual: " + (cuenta.getBalance() > 0 ? (retiroDto.getMoneda() == TipoMoneda.PESOS ? "ARG $ " : "USD $ ") + cuenta.getBalance() : "Usted tiene una deuda con el Banco."));

            return respuestaRetiroDto;
        }else {
            respuestaRetiroDto.setEstado("FALLIDA");
            respuestaRetiroDto.setMensaje("No se pudo realizar el retiro. Saldo insuficiente.");
            return respuestaRetiroDto;
        }
    }

    private double calcularComision(RetiroDto retiroDto) {
        if (retiroDto.getMoneda() == TipoMoneda.PESOS && Double.parseDouble(retiroDto.getMonto()) >= 1000000) {
            return 0.02 * Double.parseDouble(retiroDto.getMonto());
        } else if (retiroDto.getMoneda() == TipoMoneda.DOLARES && Double.parseDouble(retiroDto.getMonto()) >= 5000) {
            return 0.005 * Double.parseDouble(retiroDto.getMonto());
        }
        return 0;
    }

    private void save(Cuenta cuenta, Retiro retiro, String descripcion) {
        Transaccion transaccion = new Transaccion();
        transaccion.setNumeroMovimiento(retiro.getNumeroTransaccion());
        transaccion.setFecha(retiro.getFecha());
        transaccion.setMonto(-retiro.getMonto());
        transaccion.setTipo(TipoMovimiento.RETIRO);
        transaccion.setDescripcion(descripcion);
        cuenta.addToHistorial(transaccion);
    }

    private Retiro toRetiro(RetiroDto retiroDto) {
        Retiro retiro = new Retiro();
        retiro.setCuenta(retiroDto.getCuenta());
        retiro.setMonto(Double.parseDouble(retiroDto.getMonto()));
        retiro.setMoneda(retiroDto.getMoneda().toString());
        return retiro;
    }
}
