package ar.edu.utn.frbb.tup.proyectoFinal.model;
import java.time.LocalDate;
import java.util.Random;

public class Transaccion {
    private long numeroMovimiento;
    private LocalDate fecha;
    private double monto;
    private TipoMovimiento tipo;
    private String descripcion;

    // Constructor con parámetros
    public Transaccion(long numeroMovimiento, LocalDate fecha, double monto, TipoMovimiento tipo, String descripcion) {
        this.numeroMovimiento = numeroMovimiento;
        this.fecha = fecha;
        this.monto = monto;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    // Constructor vacío
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

}


