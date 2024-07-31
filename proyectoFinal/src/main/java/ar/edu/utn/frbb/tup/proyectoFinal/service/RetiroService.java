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

    @Autowired
    private Transaccion transaccion;

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
        double comision = transaccion.calcularComision(retiroDto);
        if (cuenta.getBalance() >= monto) {

            cuentaService.actualizarBalance(cuenta, monto, comision, TipoMovimiento.RETIRO);

            transaccion.save(cuenta, retiro, TipoMovimiento.RETIRO, "Retiro de la cuenta " + cuenta.getNumeroCuenta() + ".");

            respuestaRetiroDto.setEstado("EXITOSA");
            respuestaRetiroDto.setMensaje("Se realizó el retiro exitosamente. Número de transaccion: " + retiro.getNumeroTransaccion() + ". Realizado el " + retiro.getFecha() + ". Saldo actual: " + (cuenta.getBalance() > 0 ? (retiroDto.getMoneda() == TipoMoneda.PESOS ? "ARG $ " : "USD $ ") + cuenta.getBalance() : "Usted tiene una deuda con el Banco."));

            return respuestaRetiroDto;
        }else {
            respuestaRetiroDto.setEstado("FALLIDA");
            respuestaRetiroDto.setMensaje("No se pudo realizar el retiro. Saldo insuficiente.");
            return respuestaRetiroDto;
        }
    }

    private Retiro toRetiro(RetiroDto retiroDto) {
        Retiro retiro = new Retiro();
        retiro.setCuenta(retiroDto.getCuenta());
        retiro.setMonto(Double.parseDouble(retiroDto.getMonto()));
        retiro.setMoneda(retiroDto.getMoneda().toString());
        return retiro;
    }
}
