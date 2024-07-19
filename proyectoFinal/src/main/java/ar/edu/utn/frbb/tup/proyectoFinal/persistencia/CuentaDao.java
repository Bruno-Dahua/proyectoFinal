package ar.edu.utn.frbb.tup.proyectoFinal.persistencia;

import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.entity.CuentaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class CuentaDao  extends AbstractBaseDao{
    @Override
    protected String getEntityName() {
        return "CUENTA";
    }

    public void save(Cuenta cuenta) {
        CuentaEntity entity = new CuentaEntity(cuenta);
        getInMemoryDatabase().put(entity.getId(), entity);
    }

    public Cuenta find(long dni) {
        if (getInMemoryDatabase().get(dni) == null) {
            return null;
        }
        return ((CuentaEntity) getInMemoryDatabase().get(dni)).toCuenta();
    }

    public Set<Cuenta> getCuentasByCliente(long dni) {
        Set<Cuenta> cuentasDelCliente = new HashSet<>();
        for (Object object : getInMemoryDatabase().values()) {
            CuentaEntity cuenta = ((CuentaEntity) object);
            if (cuenta.getTitular().equals(dni)) {
                Cuenta cuentaModel = cuenta.toCuenta();
                if (!cuentasDelCliente.contains(cuentaModel)) {
                    cuentasDelCliente.add(cuentaModel);
                }
            }
        }
        return cuentasDelCliente;
    }
}
