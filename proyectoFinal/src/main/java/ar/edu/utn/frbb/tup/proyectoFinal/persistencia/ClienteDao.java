package ar.edu.utn.frbb.tup.proyectoFinal.persistencia;

import ar.edu.utn.frbb.tup.proyectoFinal.model.Cliente;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.entity.ClienteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteDao extends AbstractBaseDao {

    @Autowired
    CuentaDao cuentaDao;

    public Cliente find(long dni)  {
        for (Object object : getInMemoryDatabase().values()) {
            ClienteEntity clienteEntity = (ClienteEntity) object;
            if (clienteEntity.getDni() == (dni)) {
                return clienteEntity.toCliente();
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

    public boolean update(Cliente cliente) {
        ClienteEntity entity = new ClienteEntity(cliente);
        return getInMemoryDatabase().put(cliente.getDni(), entity) != null;
    }

    @Override
    protected String getEntityName() {
        return "CLIENTE";
    }
}
