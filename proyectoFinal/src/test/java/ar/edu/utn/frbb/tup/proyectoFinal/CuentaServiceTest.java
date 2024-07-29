package ar.edu.utn.frbb.tup.proyectoFinal;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cliente;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoCuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoMoneda;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.ClienteDoesntExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.CuentaAlreadyExistException;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.CuentaDao;
import ar.edu.utn.frbb.tup.proyectoFinal.service.ClienteService;
import ar.edu.utn.frbb.tup.proyectoFinal.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;

class CuentaServiceTest {

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private CuentaService cuentaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDarDeAltaCuenta() throws Exception, ClienteDoesntExistException, CuentaAlreadyExistException, NotPosibleException {
        // Arrange
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuentaDto.setMoneda(TipoMoneda.PESOS);
        cuentaDto.setTitular("12345678");

        Cliente cliente = new Cliente();
        cliente.setDni("12345678");
        cliente.setCuentas(new HashSet<>()); // Inicializa el Set de cuentas

        when(clienteService.buscarClientePorDni("12345678")).thenReturn(cliente);
        when(cuentaDao.find(12345678)).thenReturn(null);

        // Act
        Cuenta cuenta = cuentaService.darDeAltaCuenta(cuentaDto);

        // Assert
        assertNotNull(cuenta);
        assertEquals(Long.valueOf(12345678), cuenta.getNumeroCuenta()); // Compara con Long
        verify(cuentaDao).save(cuenta);
    }



}
