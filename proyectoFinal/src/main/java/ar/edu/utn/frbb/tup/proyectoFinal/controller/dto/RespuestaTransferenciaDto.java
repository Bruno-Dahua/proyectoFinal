package ar.edu.utn.frbb.tup.proyectoFinal.controller.dto;

public class RespuestaTransferenciaDto {
    private String estado;
    private String mensaje;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
