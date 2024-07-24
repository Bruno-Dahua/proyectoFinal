package ar.edu.utn.frbb.tup.proyectoFinal.persistencia.entity;

import ar.edu.utn.frbb.tup.proyectoFinal.model.Transferencia;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransferenciaEntity extends BaseEntity{
    long numeroTransaccion;
    String cuentaOrigen;
    String cuentaDestino;
    double monto;
    String moneda;
    LocalDate fecha;

    public TransferenciaEntity(Transferencia transferencia) {
        super(transferencia.getNumeroTransaccion());
        this.numeroTransaccion = numeroTransaccion;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.monto = monto;
        this.moneda = moneda;
        this.fecha = fecha;
    }

    public Transferencia toTransferencia() {
        Transferencia transferencia = new Transferencia();
        transferencia.setNumeroTransaccion(this.numeroTransaccion);
        transferencia.setCuentaOrigen(this.cuentaOrigen);
        transferencia.setCuentaDestino(this.cuentaDestino);
        transferencia.setMonto(this.monto);
        transferencia.setMoneda(this.moneda);
        transferencia.setFecha(this.fecha);
        return transferencia;
    }
}
