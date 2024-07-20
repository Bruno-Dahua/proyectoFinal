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
        Set<Cuenta> cuentasCliente = new HashSet<>();

        for (Object object : getInMemoryDatabase().values()){

            CuentaEntity cuentaEntity = (CuentaEntity) object;

            if(cuentaEntity.getTitular() == dni){
                cuentasCliente.add(cuentaEntity.toCuenta());
            }
        }

        return cuentasCliente;
    }

    public void update(Cuenta cuenta){
        CuentaEntity cuentaEntity = new CuentaEntity(cuenta);
        getInMemoryDatabase().put(cuenta.getNumeroCuenta(), cuentaEntity);
    }

    public boolean eliminarCuentaByDniAndNumeroCuenta(long dni, long numeroCuenta){
        Cuenta cuenta = findCuentaByDniAndNumeroCuenta(dni, numeroCuenta);
        if (cuenta != null) {
            getDatabase().remove(numeroCuenta);
            return true;
        }
        return false;
    }

    private Cuenta findCuentaByDniAndNumeroCuenta(long dni, long numeroCuenta) {
        for (Object obj : getDatabase().values()) {
            if (obj instanceof Cuenta) {
                Cuenta cuenta = (Cuenta) obj;
                if (cuenta.getTitular().getDni() == dni && cuenta.getNumeroCuenta() == numeroCuenta) {
                    return cuenta;
                }
            }
        }
        return null;
    }
}
