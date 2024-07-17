package ar.edu.utn.frbb.tup.proyectoFinal.model;

import java.time.LocalDate;
import java.time.Period;

public class Persona {
    private String nombre;
    private String apellido;
    private String dni;
    private String fechaNacimiento;

    public Persona() {}
    public Persona(String dni, String apellido, String nombre, String fechaNacimiento) {
        this.dni = dni;
        this.apellido = apellido;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public long getDni() {
        return Long.parseLong(dni);
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getDniString(){
        return dni;
    }


    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getEdad() {
        LocalDate fechaHoy = LocalDate.now();
        LocalDate fechaNacimiento = LocalDate.parse(getFechaNacimiento());
        Period edad = Period.between(fechaNacimiento, fechaHoy);
        return edad.getYears();
    }

}
