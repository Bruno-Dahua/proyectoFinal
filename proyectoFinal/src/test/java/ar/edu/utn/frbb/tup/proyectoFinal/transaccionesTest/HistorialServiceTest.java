package ar.edu.utn.frbb.tup.proyectoFinal.transaccionesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.RespuestaHistorialDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Cuenta;
import ar.edu.utn.frbb.tup.proyectoFinal.model.TipoMovimiento;
import ar.edu.utn.frbb.tup.proyectoFinal.model.Transaccion;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.NotPosibleException;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.CuentaDao;
import ar.edu.utn.frbb.tup.proyectoFinal.service.HistorialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class HistorialServiceTest {

    @Mock
    private CuentaDao cuentaDao;

    @InjectMocks
    private HistorialService historialService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerHistorialCuentaExistente() throws NotPosibleException {
        long numeroCuenta = 45889159L;
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(numeroCuenta);
        Set<Transaccion> transacciones = new HashSet<>();
        Transaccion transaccion = new Transaccion();
        transaccion.setNumeroMovimiento(1);
        transaccion.setFecha(LocalDate.now());
        transaccion.setMonto(100.0);
        transaccion.setTipo(TipoMovimiento.DEPOSITO);
        transaccion.setDescripcion("Depósito");
        transacciones.add(transaccion);
        cuenta.setHistorialTransacciones(transacciones);

        when(cuentaDao.findByNumeroCuenta(numeroCuenta)).thenReturn(cuenta);

        RespuestaHistorialDto respuesta = historialService.obtenerHistorial(numeroCuenta);

        assertEquals(numeroCuenta, respuesta.getNumeroCuenta());
        assertEquals(1, respuesta.getHistorialTransacciones().size());
    }

    @Test
    public void testObtenerHistorialCuentaNoExistente() {
        long numeroCuenta = 45889159L;

        when(cuentaDao.findByNumeroCuenta(numeroCuenta)).thenReturn(null);

        // Ejecutar y verificar que se lanza la excepción correspondiente
        assertThrows(NotPosibleException.class, () -> {
            historialService.obtenerHistorial(numeroCuenta);
        });
    }
}

