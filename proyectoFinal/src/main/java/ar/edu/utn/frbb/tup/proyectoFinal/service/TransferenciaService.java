package ar.edu.utn.frbb.tup.proyectoFinal.service;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.RespuestaTransaccionDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.TransferenciaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.*;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.CuentaDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.ClienteDao;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TransferenciaService {
    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private BanelcoService banelcoService;

    @Autowired
    private Transaccion transaccion;

    public RespuestaTransaccionDto realizarTransferencia(TransferenciaDto transferenciaDto) throws ClienteDoesntExistException, NotPosibleException, CuentaDoesntExistException {

        Cuenta cuentaOrigen = cuentaDao.findByNumeroCuenta(Long.parseLong(transferenciaDto.getCuentaOrigen()));
        if (cuentaOrigen == null) {
            throw new CuentaDoesntExistException("La cuenta " + transferenciaDto.getCuentaOrigen() + " (cuenta de origen) no existe.");
        }

        Cuenta cuentaDestino = cuentaDao.findByNumeroCuenta(Long.parseLong(transferenciaDto.getCuentaDestino()));
        if (cuentaDestino == null) {
            throw new CuentaDoesntExistException("La cuenta " + transferenciaDto.getCuentaDestino() + " (cuenta de destino) no existe.");
        }

        if (!cuentaOrigen.getMoneda().equals(cuentaDestino.getMoneda()) || !cuentaOrigen.getMoneda().equals(transferenciaDto.getMoneda()) || !cuentaDestino.getMoneda().equals(transferenciaDto.getMoneda())) {
            throw new NotPosibleException("Las monedas de las cuentas no coinciden.");
        }


        if ((cuentaOrigen.getTitular().getBanco()).equals(cuentaDestino.getTitular().getBanco())) {
            return realizarTransferenciaYActualizarBalance(transferenciaDto, cuentaOrigen, cuentaDestino);
        } else {
            if (banelcoService.servicioDeBanelco(transferenciaDto)) {
                return realizarTransferenciaYActualizarBalance(transferenciaDto, cuentaOrigen, cuentaDestino);
            } else {
                RespuestaTransaccionDto respuestaTransferenciaDto = new RespuestaTransaccionDto();
                respuestaTransferenciaDto.setEstado("FALLIDA");
                respuestaTransferenciaDto.setMensaje("No es posible realizar la transferencia, los bancos son diferentes.");
                return respuestaTransferenciaDto;
            }
        }
    }


    private RespuestaTransaccionDto realizarTransferenciaYActualizarBalance(TransferenciaDto transferenciaDto, Cuenta cuentaOrigen, Cuenta cuentaDestino) {
        RespuestaTransaccionDto respuestaTransferenciaDto = new RespuestaTransaccionDto();

        Transferencia transferencia = toTransferencia(transferenciaDto);

        if (cuentaOrigen.getBalance() >= Double.parseDouble(transferenciaDto.getMonto())) {
            double monto = Double.parseDouble(transferenciaDto.getMonto());
            double comision = transaccion.calcularComision(transferenciaDto);

            cuentaService.actualizarBalance(cuentaOrigen, monto, comision, TipoMovimiento.TRANSFERENCIA_SALIDA);
            cuentaService.actualizarBalance(cuentaDestino, monto, comision, TipoMovimiento.TRANSFERENCIA_ENTRADA);

            // Creo y agrego las transacciones al historial
            transaccion.save(cuentaOrigen, transferencia, TipoMovimiento.TRANSFERENCIA_SALIDA, "Transferencia a cuenta " + cuentaDestino.getNumeroCuenta());
            transaccion.save(cuentaDestino, transferencia, TipoMovimiento.TRANSFERENCIA_ENTRADA, "Transferencia desde cuenta " + cuentaOrigen.getNumeroCuenta());

            // Actualizo las cuentas en la base de datos
            cuentaDao.update(cuentaOrigen);
            cuentaDao.update(cuentaDestino);

            // Preparo una respuestaDto
            respuestaTransferenciaDto.setEstado("EXITOSA");
            respuestaTransferenciaDto.setMensaje("Se realizó la transferencia exitosamente. Número de transferencia: " + transferencia.getNumeroTransaccion() + ". Realizado el " + transferencia.getFecha() + ". Saldo actual: " + (cuentaOrigen.getBalance() > 0 ? (transferenciaDto.getMoneda() == TipoMoneda.PESOS ? "ARG $ " : "USD $ ") + cuentaOrigen.getBalance() : "Usted tiene una deuda con el Banco."));
            return respuestaTransferenciaDto;
        } else {
            respuestaTransferenciaDto.setEstado("FALLIDA");
            respuestaTransferenciaDto.setMensaje("Saldo insuficiente para realizar la transferencia.");
            return respuestaTransferenciaDto;
        }
    }

    private Transferencia toTransferencia(TransferenciaDto transferenciaDto) {
        Transferencia transferencia = new Transferencia();
        transferencia.setMonto(Double.parseDouble(transferenciaDto.getMonto()));
        transferencia.setCuentaOrigen(transferenciaDto.getCuentaOrigen());
        transferencia.setCuentaDestino(transferenciaDto.getCuentaDestino());
        transferencia.setMoneda(transferenciaDto.getMoneda().toString());
        return transferencia;
    }
}
