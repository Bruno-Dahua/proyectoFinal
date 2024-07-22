package ar.edu.utn.frbb.tup.proyectoFinal.service;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.RespuestaTransferenciaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.TransferenciaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.*;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.ClienteDao;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.TransferenciaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class TransferenciaService {
    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    private TransferenciaDao transferenciaDao;


    public RespuestaTransferenciaDto realizarTransferencia(TransferenciaDto transferenciaDto) throws ClienteDoesntExistException, NotPosibleException {
        Cliente clienteOrigen = clienteDao.find(Long.parseLong(transferenciaDto.getCuentaOrigen()), false);
        Set<Cuenta> cuentasOrigen = clienteOrigen != null ? clienteOrigen.getCuentas() : null;
        Cuenta cuentaOrigen = null;

        Cliente clienteDestino = clienteDao.find(Long.parseLong(transferenciaDto.getCuentaDestino()), false);
        Set<Cuenta> cuentasDestino = clienteDestino != null ? clienteDestino.getCuentas() : null;
        Cuenta cuentaDestino = null;

        if (clienteOrigen == null) {
            throw new ClienteDoesntExistException("El cliente con DNI " + transferenciaDto.getCuentaOrigen() + " no existe.");
        } else if (clienteDestino == null) {
            throw new ClienteDoesntExistException("El cliente con DNI " + transferenciaDto.getCuentaDestino() + " no existe.");
        } else if (cuentasDestino == null || cuentasOrigen == null) {
            throw new NotPosibleException("El cliente no tiene cuentas.");
        }

        Transferencia transferencia = toTransferencia(transferenciaDto);
        RespuestaTransferenciaDto respuestaTransferenciaDto = new RespuestaTransferenciaDto();

        if (transferenciaDto.getMoneda() == TipoMoneda.PESOS) {
            for (Cuenta c : cuentasOrigen) {
                if (c.getTipoCuenta() == TipoCuenta.CUENTA_CORRIENTE && c.getMoneda() == TipoMoneda.PESOS) {
                    cuentaOrigen = c;
                    break;
                }
            }

            for (Cuenta c : cuentasDestino) {
                if (c.getTipoCuenta() == TipoCuenta.CUENTA_CORRIENTE && c.getMoneda() == TipoMoneda.PESOS) {
                    cuentaDestino = c;
                    break;
                }
            }

            if (cuentaOrigen != null && cuentaDestino != null && cuentaOrigen.getBalance() >= transferencia.getMonto()) {
                if (transferenciaDto.getMonto() >= 1000000) {
                    cuentaOrigen.setBalance(cuentaOrigen.getBalance() - transferencia.getMonto() - (0.02 * transferencia.getMonto()));
                    cuentaDestino.setBalance(cuentaDestino.getBalance() + transferencia.getMonto());
                } else {
                    cuentaOrigen.setBalance(cuentaOrigen.getBalance() - transferencia.getMonto());
                    cuentaDestino.setBalance(cuentaDestino.getBalance() + transferencia.getMonto());
                }
                transferenciaDao.realizar(transferencia);
                respuestaTransferenciaDto.setEstado("EXITOSA");
                if (cuentaOrigen.getBalance() > 0) {
                    respuestaTransferenciaDto.setMensaje("Se realizó la transferencia exitosamente. Número de transferencia: " + transferencia.getNumeroTransaccion() + ". Realizado el " + transferencia.getFecha() + ". Saldo actual: ARG $ " + cuentaOrigen.getBalance());
                }else {
                    respuestaTransferenciaDto.setMensaje("Se realizó la transferencia exitosamente. Número de transferencia: " + transferencia.getNumeroTransaccion() + ". Realizado el " + transferencia.getFecha() + ". Usted tiene una deuda con el Banco.");
                }
                return respuestaTransferenciaDto;
            } else {
                respuestaTransferenciaDto.setEstado("FALLIDA");
                respuestaTransferenciaDto.setMensaje("No fue posible realizar la transferencia.");
                return respuestaTransferenciaDto;
            }
        }

        else {
            for (Cuenta c : cuentasOrigen) {
                if (c.getTipoCuenta() == TipoCuenta.CAJA_AHORRO && c.getMoneda() == TipoMoneda.DOLARES) {
                    cuentaOrigen = c;
                }
            }

            for (Cuenta c : cuentasDestino) {
                if (c.getTipoCuenta() == TipoCuenta.CAJA_AHORRO && c.getMoneda() == TipoMoneda.DOLARES) {
                    cuentaDestino = c;
                }
            }

            if (cuentaOrigen != null && cuentaDestino != null && cuentaOrigen.getBalance() >= transferencia.getMonto()) {
                if (transferenciaDto.getMonto() >= 5000) {
                    cuentaOrigen.setBalance(cuentaOrigen.getBalance() - transferencia.getMonto() - (0.005 * transferencia.getMonto()));
                    cuentaDestino.setBalance(cuentaDestino.getBalance() + transferencia.getMonto());
                } else {
                    cuentaOrigen.setBalance(cuentaOrigen.getBalance() - transferencia.getMonto());
                    cuentaDestino.setBalance(cuentaDestino.getBalance() + transferencia.getMonto());
                }
                transferenciaDao.realizar(transferencia);
                respuestaTransferenciaDto.setEstado("EXITOSA");
                if (cuentaOrigen.getBalance() > 0) {
                    respuestaTransferenciaDto.setMensaje("Se realizo la transferencia exitosamente. Numero de transferencia: " + transferencia.getNumeroTransaccion() + ". Ralizado el " + transferencia.getFecha() + ". Saldo actual: USD $ " + cuentaOrigen.getBalance());
                }else {
                    respuestaTransferenciaDto.setMensaje("Se realizo la transferencia exitosamente. Numero de transferencia: " + transferencia.getNumeroTransaccion() + ". Ralizado el " + transferencia.getFecha() + ". Usted tiene una deuda con el Banco.");
                }
                return respuestaTransferenciaDto;
            }else {
                respuestaTransferenciaDto.setEstado("FALLIDO");
                respuestaTransferenciaDto.setMensaje("No fue posible realizar la transferencia.");
                return respuestaTransferenciaDto;
            }
        }
    }

    private Transferencia toTransferencia(TransferenciaDto transferenciaDto) {
        Transferencia transferencia = new Transferencia();
        transferencia.setMonto(transferenciaDto.getMonto());
        transferencia.setCuentaOrigen(transferenciaDto.getCuentaOrigen());
        transferencia.setCuentaDestino(transferenciaDto.getCuentaDestino());
        transferencia.setMoneda(transferenciaDto.getMoneda().toString());
        return transferencia;
    }
}
