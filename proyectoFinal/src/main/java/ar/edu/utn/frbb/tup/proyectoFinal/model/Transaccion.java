package ar.edu.utn.frbb.tup.proyectoFinal.model;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.DepositoDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.RetiroDto;
import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.TransferenciaDto;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Component
public class Transaccion {
    private long numeroMovimiento;
    private LocalDate fecha;
    private double monto;
    private TipoMovimiento tipo;
    private String descripcion;

    public Transaccion(long numeroMovimiento, LocalDate fecha, double monto, TipoMovimiento tipo, String descripcion) {
        this.numeroMovimiento = numeroMovimiento;
        this.fecha = fecha;
        this.monto = monto;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    public Transaccion() {
    }

    public long getNumeroMovimiento() {
        return numeroMovimiento;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getMonto() {
        return monto;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setNumeroMovimiento(long numeroMovimiento) {
        this.numeroMovimiento = numeroMovimiento;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void save(Cuenta cuenta, Object operacion, TipoMovimiento tipo, String descripcion) {
        Transaccion transaccion = new Transaccion();

        if (operacion instanceof Deposito) {
            Deposito deposito = (Deposito) operacion;
            transaccion.setNumeroMovimiento(deposito.getNumeroTransaccion());
            transaccion.setFecha(deposito.getFecha());
            transaccion.setMonto(deposito.getMonto());
            transaccion.setTipo(TipoMovimiento.DEPOSITO);
        } else if (operacion instanceof Transferencia) {
            Transferencia transferencia = (Transferencia) operacion;
            transaccion.setNumeroMovimiento(transferencia.getNumeroTransaccion());
            transaccion.setFecha(transferencia.getFecha());
            transaccion.setMonto(tipo == TipoMovimiento.TRANSFERENCIA_SALIDA ? -transferencia.getMonto() : transferencia.getMonto());
            transaccion.setTipo(tipo);
        } else if (operacion instanceof Retiro) {
            Retiro retiro = (Retiro) operacion;
            transaccion.setNumeroMovimiento(retiro.getNumeroTransaccion());
            transaccion.setFecha(retiro.getFecha());
            transaccion.setMonto(-retiro.getMonto());
            transaccion.setTipo(TipoMovimiento.RETIRO);
        }

        transaccion.setDescripcion(descripcion);
        cuenta.addToHistorial(transaccion);
    }

    public double calcularComision(DepositoDto depositoDto) {
        return calcularComision(depositoDto.getMoneda(), Double.parseDouble(depositoDto.getMonto()));
    }

    public double calcularComision(TransferenciaDto transferenciaDto) {
        return calcularComision(transferenciaDto.getMoneda(), Double.parseDouble(transferenciaDto.getMonto()));
    }

    public double calcularComision(RetiroDto retiroDto) {
        return calcularComision(retiroDto.getMoneda(), Double.parseDouble(retiroDto.getMonto()));
    }

    private double calcularComision(TipoMoneda moneda, double monto) {
        if (moneda == TipoMoneda.PESOS && monto >= 1000000) {
            return 0.02 * monto;
        } else if (moneda == TipoMoneda.DOLARES && monto >= 5000) {
            return 0.005 * monto;
        }
        return 0;
    }

    public double calcularNuevoBalance(double balanceActual, double monto, double comision, TipoMovimiento tipo) {
        switch (tipo) {
            case TRANSFERENCIA_SALIDA, RETIRO:
                return balanceActual - monto - comision;
            case DEPOSITO, TRANSFERENCIA_ENTRADA:
                return balanceActual + monto;
            default:
                throw new IllegalArgumentException("Tipo de movimiento no soportado");
        }
    }

}


