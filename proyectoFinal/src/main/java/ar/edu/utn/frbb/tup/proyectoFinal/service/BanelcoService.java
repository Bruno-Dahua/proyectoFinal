package ar.edu.utn.frbb.tup.proyectoFinal.service;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.TransferenciaDto;
import org.springframework.stereotype.Service;

@Service
public class BanelcoService {
    public boolean servicioDeBanelco(TransferenciaDto transferenciaDto){
        return Long.parseLong(transferenciaDto.getCuentaDestino()) % 2 == 0;
    }
}
