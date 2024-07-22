package ar.edu.utn.frbb.tup.proyectoFinal.persistencia;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.TransferenciaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Transferencia;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.entity.TransferenciaEntity;

public class TransferenciaDao extends AbstractBaseDao {

    public void realizar(Transferencia transferencia) {
        TransferenciaEntity transferenciaEntity = new TransferenciaEntity(transferencia);
        getInMemoryDatabase().put(transferenciaEntity.getId(), transferenciaEntity);
    }

    @Override
    protected String getEntityName() {
        return "TRANSFERENCIA";
    }
}
