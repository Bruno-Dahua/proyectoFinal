package ar.edu.utn.frbb.tup.proyectoFinal.persistencia;

import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoCuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.entity.CuentaEntity;
import org.springframework.stereotype.Component;

import java.util.*;

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

            if(cuentaEntity.getTitular().getDni() == dni){
                cuentasCliente.add(cuentaEntity.toCuenta());
            }
        }

        return cuentasCliente;
    }

    public void update(Cuenta cuenta){
        CuentaEntity cuentaEntity = new CuentaEntity(cuenta);
        getInMemoryDatabase().put(cuenta.getNumeroCuenta(), cuentaEntity);
    }


    public Cuenta findByNumeroCuenta(long numeroCuenta) {
        for (Object object : getInMemoryDatabase().values()) {
            CuentaEntity cuentaEntity = (CuentaEntity) object;
            if (cuentaEntity.getNumeroCuenta() == numeroCuenta) {
                return cuentaEntity.toCuenta();
            }
        }
        return null;
    }

    public Cuenta obtenerCuentaPrioritaria(Set<Cuenta> cuentas, long dni) {
        Cuenta cuentaCorriente = null;
        Cuenta cajaAhorro = null;

        for (Cuenta cuenta : cuentas) {
            if (cuenta.getTitular().getDni() == dni) {
                if (cuenta.getTipoCuenta() == TipoCuenta.CUENTA_CORRIENTE) {
                    cuentaCorriente = cuenta;
                } else if (cuenta.getTipoCuenta() == TipoCuenta.CAJA_AHORRO) {
                    cajaAhorro = cuenta;
                }
            }
        }

        return cuentaCorriente != null ? cuentaCorriente : cajaAhorro;
    }
}
