package ar.edu.utn.frbb.tup.proyectoFinal.persistencia.entity;

import ar.edu.utn.frbb.tup.proyectoFinal.model.Cliente;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ClienteEntity extends BaseEntity {

    private final Long dni;
    private final String tipoPersona;
    private final String nombre;
    private final String apellido;
    private final LocalDate fechaAlta;
    private final LocalDate fechaNacimiento;
    private final String banco;
    private List<Cuenta> cuentas;

    public ClienteEntity(Cliente cliente) {
        super(cliente.getDni());
        this.dni = cliente.getDni();
        this.tipoPersona = cliente.getTipoPersona();
        this.nombre = cliente.getNombre();
        this.apellido = cliente.getApellido();
        this.fechaAlta = cliente.getFechaAlta();
        this.fechaNacimiento = cliente.getFechaNacimiento();
        this.banco = cliente.getBanco();
        this.cuentas = new ArrayList<>();
        if (cliente.getCuentas() != null && !cliente.getCuentas().isEmpty()) {
            cuentas.addAll(cliente.getCuentas());
        }
    }


    public Cliente toCliente() {
        Cliente cliente = new Cliente();
        cliente.setDni(this.getId());
        cliente.setNombre(this.nombre);
        cliente.setApellido(this.apellido);
        cliente.setTipoPersona(this.tipoPersona);
        cliente.setFechaAlta(this.fechaAlta);
        cliente.setFechaNacimiento(this.fechaNacimiento);
        cliente.setBanco(this.banco);

        cliente.setCuentas(new HashSet<>(cuentas));

        return cliente;
    }

    public Long getDni() {
        return dni;
    }
}
