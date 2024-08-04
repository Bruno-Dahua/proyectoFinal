package ar.edu.utn.frbb.tup.proyectoFinal;

import static java.nio.file.Paths.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ar.edu.utn.frbb.tup.proyectoFinal.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.proyectoFinal.model.*;
import ar.edu.utn.frbb.tup.proyectoFinal.model.exceptions.*;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.ClienteDao;
import ar.edu.utn.frbb.tup.proyectoFinal.persistencia.CuentaDao;
import ar.edu.utn.frbb.tup.proyectoFinal.service.ClienteService;
import ar.edu.utn.frbb.tup.proyectoFinal.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ClienteServiceTest {
    @Mock
    private ClienteDao clienteDao;

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private ClienteService clienteService;

    private ClienteDto clienteDto;
    private Cliente cliente;
    private Cuenta cuenta;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        clienteDto = new ClienteDto();
        clienteDto.setDni(458889159L);
        clienteDto.setNombre("Bruno");
        clienteDto.setApellido("Dahua");
        clienteDto.setFechaNacimiento(String.valueOf(LocalDate.of(2005, 01, 03)));
        clienteDto.setTipoPersona("PERSONA_FISICA");

        cliente = new Cliente();
        cliente.setDni(18458131L);
        cliente.setNombre("Diana");
        cliente.setApellido("Juan");
        cliente.setFechaNacimiento(LocalDate.parse("1967-12-08"));
        cliente.setTipoPersona(String.valueOf(TipoPersona.PERSONA_FISICA));

        cuenta = new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuenta.setMoneda(TipoMoneda.PESOS);
        cuenta.setTitular(cliente);

    }

//POST

    @Test
    public void testDarDeAltaCliente_Success() throws ClienteDoesntExistException, NotPosibleException {
        // Lo busco. No lo encuentro, retorno null.
        when(clienteDao.find(45889159L)).thenReturn(null);

        // Simulo el retorno de la llamada a la base de datos.
        clienteService.darDeAltaCliente(clienteDto);

        // Verifico que se haya llamado una vez.
        verify(clienteDao, times(1)).save(any(Cliente.class));
    }

    @Test
    public void testDarDeAltaCliente_ClienteExists() throws ClienteDoesntExistException {
        // Modifica los datos del clienteDto para esta prueba específica
        clienteDto.setDni(45889159L);

        // Lo busco. Lo encuentro, retorno el cliente.
        when(clienteDao.find(45889159L)).thenReturn(cliente);

        assertThrows(NotPosibleException.class, () -> clienteService.darDeAltaCliente(clienteDto));
    }

    @Test
    public void testDarDeAltaCliente_UnderageClient() throws ClienteDoesntExistException {
        // Modifica los datos del clienteDto para esta prueba específica
        clienteDto.setDni(45889159L);
        clienteDto.setFechaNacimiento("2023-01-03");

        // Lo busco. No lo encuentro, retorno null.
        when(clienteDao.find(45889159L)).thenReturn(null);

        assertThrows(NotPosibleException.class, () -> clienteService.darDeAltaCliente(clienteDto));
    }


//GET

    @Test
    void testBuscarClientePorDni_ClienteExiste() throws ClienteDoesntExistException {
        long dni = cliente.getDni();

        when(clienteDao.find(dni)).thenReturn(cliente);

        Cliente result = clienteService.buscarClientePorDni(dni);

        assertNotNull(result);
        assertEquals(dni, result.getDni());
    }


    @Test
    void testBuscarClientePorDni_ClienteNoExiste() throws ClienteDoesntExistException {
        long dni = 45889159L;

        when(clienteDao.find(dni)).thenReturn(null);

        ClienteDoesntExistException thrown = assertThrows(ClienteDoesntExistException.class, () -> {
            clienteService.buscarClientePorDni(dni);
        });
        assertEquals("No existe el cliente con DNI " + dni + ".", thrown.getMessage());
    }


//DELETE

    @Test
    void testEliminarCliente() throws ClienteDoesntExistException {
        long dni = cliente.getDni();

        when(clienteDao.find(dni)).thenReturn(cliente);
        when(clienteDao.delete(dni)).thenReturn(true);

        clienteService.eliminarCliente(dni);

        verify(clienteDao).find(dni);
        verify(clienteDao).delete(dni);
    }


    @Test
    void testEliminarCliente_NoExiste() throws ClienteDoesntExistException {
        long dni = 45889159L;

        when(clienteDao.find(dni)).thenReturn(null);

        ClienteDoesntExistException thrown = assertThrows(ClienteDoesntExistException.class, () -> {
            clienteService.eliminarCliente(dni);
        });

        assertEquals("No existe el cliente con DNI " + dni + ".", thrown.getMessage());
    }


//PUT

    @Test
    public void actualizarCliente() throws ClienteDoesntExistException, NotPosibleException {
        long dniAntiguo = 38944251;

        when(clienteDao.find(dniAntiguo)).thenReturn(cliente);
        when(cuentaDao.getCuentasByCliente(dniAntiguo)).thenReturn(new HashSet<>());

        clienteService.actualizarCliente(dniAntiguo, clienteDto);

        verify(cuentaService, times(0)).actualizarTitularCuenta(any(Cliente.class), eq(dniAntiguo));
        verify(clienteDao, times(1)).delete(dniAntiguo);
        verify(clienteDao, times(1)).update(any(Cliente.class));
    }

    @Test
    public void testActualizarClienteNoExiste() {
        long dniAntiguo = 12345678L;
        ClienteDto clienteDto = new ClienteDto();

        when(clienteDao.find(dniAntiguo)).thenReturn(null);

        ClienteDoesntExistException thrown = assertThrows(
                ClienteDoesntExistException.class,
                () -> clienteService.actualizarCliente(dniAntiguo, clienteDto)
        );
        assertEquals("No existe el cliente con DNI 12345678.", thrown.getMessage());
    }

    @Test
    public void testActualizarClienteErrorCuentaService() {
        long dniAntiguo = 12345678L;
        ClienteDto clienteDto = new ClienteDto();
        Cliente clienteExistente = new Cliente();
        clienteExistente.setFechaNacimiento(LocalDate.parse("2005-01-03"));
        clienteExistente.setFechaAlta(LocalDate.now());

        when(clienteDao.find(dniAntiguo)).thenReturn(clienteExistente);
        doThrow(new RuntimeException()).when(cuentaService).actualizarTitularCuenta(any(Cliente.class), eq(dniAntiguo));

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> clienteService.actualizarCliente(dniAntiguo, clienteDto)
        );
    }
}
