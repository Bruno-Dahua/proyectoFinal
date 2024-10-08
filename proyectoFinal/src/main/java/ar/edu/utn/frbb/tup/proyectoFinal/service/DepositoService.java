package ar.edu.utn.frbb.tup.proyectoFinal.service;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.DepositoDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.RespuestaTransaccionDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.*;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.CuentaDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepositoService {

    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private Transaccion transaccion;

    public RespuestaTransaccionDto realizarDeposito(DepositoDto depositoDto) throws ClienteDoesntExistException, CuentaDoesntExistException, NotPosibleException {
        Cuenta cuenta = cuentaDao.findByNumeroCuenta(depositoDto.getCuenta());

        if (cuenta == null) {
            throw new CuentaDoesntExistException("La cuenta " + depositoDto.getCuenta() + " no existe.");
        }

        if (!(String.valueOf(cuenta.getMoneda())).equals(depositoDto.getMoneda())) {
            throw new NotPosibleException("Las monedas de las cuentas no coinciden");
        }
        return realizarDepositoYActualizarBalance(cuenta, depositoDto);
    }

    private RespuestaTransaccionDto realizarDepositoYActualizarBalance(Cuenta cuenta, DepositoDto depositoDto) throws NotPosibleException {
        RespuestaTransaccionDto respuestaDepositoDto = new RespuestaTransaccionDto();

        Deposito deposito = toDeposito(depositoDto);

        double comision = transaccion.calcularComision(depositoDto);

        cuentaService.actualizarBalance(cuenta, depositoDto.getMonto(), comision, TipoMovimiento.DEPOSITO);

        transaccion.save(cuenta, deposito, TipoMovimiento.DEPOSITO, "Deposito a la cuenta " + cuenta.getNumeroCuenta() + ".");

        respuestaDepositoDto.setEstado("EXITOSA");
        respuestaDepositoDto.setMensaje("Se realizó el deposito exitosamente. Número de transaccion: " + deposito.getNumeroTransaccion() + ". Realizado el " + deposito.getFecha() + ". Saldo actual: " + (cuenta.getBalance() > 0 ? ((TipoMoneda.valueOf(depositoDto.getMoneda()) == TipoMoneda.PESOS ? "ARG $ " : "USD $ ") + cuenta.getBalance()) : "Usted tiene una deuda con el Banco."));

        return respuestaDepositoDto;
    }

    private Deposito toDeposito(DepositoDto depositoDto) {
        Deposito deposito = new Deposito();
        deposito.setCuenta(depositoDto.getCuenta());
        deposito.setMonto(depositoDto.getMonto());
        deposito.setMoneda(depositoDto.getMoneda());
        return deposito;
    }
}
