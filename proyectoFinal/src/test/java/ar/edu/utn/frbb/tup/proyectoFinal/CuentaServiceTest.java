package ar.edu.utn.frbb.tup.proyectoFinal;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.CuentaDao;
import ar.edu.utn.frbb.tup.proyectoFinal.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CuentaServiceTest {

    @Mock
    private CuentaDao cuentaDao;

    @InjectMocks
    private CuentaService cuentaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//GET /numeroCuenta
    @Test
    void testBuscarCuentaPorNumeroCuenta_Success() throws NotPosibleException {
        long numeroCuenta = 123456789L;
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(numeroCuenta);

        when(cuentaDao.findByNumeroCuenta(numeroCuenta)).thenReturn(cuenta);

        Cuenta result = cuentaService.buscarCuentaPorNumeroCuenta(numeroCuenta);
        assertEquals(cuenta, result);
    }

    @Test
    void testBuscarCuentaPorNumeroCuenta_NotFound() {
        long numeroCuenta = 123456789L;

        when(cuentaDao.findByNumeroCuenta(numeroCuenta)).thenReturn(null);

        assertThrows(NotPosibleException.class, () -> {
            cuentaService.buscarCuentaPorNumeroCuenta(numeroCuenta);
        });
    }
}

