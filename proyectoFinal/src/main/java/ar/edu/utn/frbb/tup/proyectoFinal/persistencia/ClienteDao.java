package ar.edu.utn.frbb.tup.proyectoFinal.persistencia;

import ar.edu.utn.frbb.tup.proyectoFinal.model.Cliente;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.entity.ClienteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ClienteDao extends AbstractBaseDao {

    @Autowired
    CuentaDao cuentaDao;

    public Cliente find(long dni) throws ClienteDoesntExistException {
        for (Object object : getInMemoryDatabase().values()) {
            ClienteEntity clienteEntity = (ClienteEntity) object;
            try {
                if (Long.parseLong(clienteEntity.getDni()) == dni) {
                    return clienteEntity.toCliente();
                }
            } catch (NumberFormatException e) {
                System.out.println("Error al convertir DNI: " + clienteEntity.getDni());
            }
        }
        return null;
    }


    public Cliente save(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity(cliente);
        getInMemoryDatabase().put(entity.getId(), entity);
        return cliente;
    }

    public boolean delete(long dni) {
        return getInMemoryDatabase().remove(dni) != null;
    }

    public void update(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity(cliente);
        getInMemoryDatabase().put(cliente.getDni(), entity);
    }

    @Override
    protected String getEntityName() {
        return "CLIENTE";
    }
}
