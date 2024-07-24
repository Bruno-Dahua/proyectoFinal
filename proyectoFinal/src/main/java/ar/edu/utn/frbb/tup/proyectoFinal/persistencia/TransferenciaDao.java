package ar.edu.utn.frbb.tup.proyectoFinal.persistencia;

import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoMovimiento;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Transaccion;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Transferencia;
import org.springframework.stereotype.Component;

@Component
public class TransferenciaDao extends AbstractBaseDao {

    public void save(Cuenta cuenta, Transferencia transferencia, TipoMovimiento tipo, String descripcion) {
        Transaccion transaccion = new Transaccion();
        transaccion.setNumeroMovimiento(transferencia.getNumeroTransaccion());
        transaccion.setFecha(transferencia.getFecha());
        transaccion.setMonto(tipo == TipoMovimiento.TRANSFERENCIA_SALIDA ? -transferencia.getMonto() : +transferencia.getMonto());
        transaccion.setTipo(tipo);
        transaccion.setDescripcion(descripcion);
        cuenta.addToHistorial(transaccion);
    }

    @Override
    protected String getEntityName() {
        return "TRANSFERENCIA";
    }
}
