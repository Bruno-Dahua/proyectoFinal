package ar.edu.utn.frbb.tup.proyectoFinal.persistencia;

import ar.edu.utn.frbb.tup.proyectoFinal.model.Cliente;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.entity.ClienteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ClienteDao extends AbstractBaseDao {

    @Autowired
    CuentaDao cuentaDao;

    public Cliente find(long dni, boolean loadComplete) {
        if (getInMemoryDatabase().get(dni) == null)
            return null;

        Cliente cliente = ((ClienteEntity) getInMemoryDatabase().get(dni)).toCliente();

        if (loadComplete) {
            Set<Cuenta> cuentas = cuentaDao.getCuentasByCliente(dni);
            // Reemplazar las cuentas del cliente solo si aún no están configuradas
            if (cliente.getCuentas().isEmpty()) {
                for (Cuenta cuenta : cuentas) {
                    cliente.addCuenta(cuenta);
                }
            }
        }

        return cliente;
    }



    public Cliente save(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity(cliente);
        getInMemoryDatabase().put(entity.getId(), entity);
        return cliente;
    }

    public boolean delete(long dni) {
        return getInMemoryDatabase().remove(dni) != null;
    }

    @Override
    protected String getEntityName() {
        return "CLIENTE";
    }
}
