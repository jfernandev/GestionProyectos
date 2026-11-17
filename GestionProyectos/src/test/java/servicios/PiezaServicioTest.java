
package servicios;

import dao.PiezaDAO;
import modelo.Pieza;
import modelo.Usuario;
import utilidades.SesionUsuario;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PiezaServicioTest {

    // DAO simulado para evitar acceder a MariaDB
    private PiezaDAO piezaDAO;

    // Servicio que vamos a probar
    private PiezaServicio piezaServicio;

    // Se ejecuta antes de cada prueba
    @BeforeEach
    void setUp() {

        piezaDAO = mock(PiezaDAO.class);

        // Creamos un administrador simulado
        Usuario administrador = new Usuario(
                "admin",
                "hash_simulado",
                "ADMIN"
        );

        // Establecemos la sesión del administrador
        SesionUsuario.establecerUsuario(administrador);

        // Inyectamos el DAO simulado
        piezaServicio = new PiezaServicio(piezaDAO);
    }

    // Se ejecuta después de cada prueba
    @AfterEach
    void tearDown() {

        // Cerramos la sesión para evitar que afecte a otras pruebas
        SesionUsuario.cerrarSesion();
    }

    // Crea una pieza con datos válidos
    private Pieza crearPieza() {

        Pieza pieza = new Pieza();

        pieza.setCodigo("PIEZA001");
        pieza.setNombre("Tornillo M8");
        pieza.setPrecio(new BigDecimal("0.25"));
        pieza.setDescripcion("Tornillo de acero inoxidable");

        return pieza;
    }

    // Comprueba que no se pueda registrar una pieza nula
    @Test
    void registrarPiezaNula() {

        assertThrows(
                IllegalArgumentException.class,
                () -> piezaServicio.registrar(null)
        );

        verify(piezaDAO, never()).guardar(any());
    }

    // Comprueba que no se pueda registrar una pieza sin código
    @Test
    void registrarPiezaSinCodigo() {

        Pieza pieza = crearPieza();
        pieza.setCodigo("");

        assertThrows(
                IllegalArgumentException.class,
                () -> piezaServicio.registrar(pieza)
        );

        verify(piezaDAO, never()).guardar(any());
    }

    // Comprueba que no se pueda registrar una pieza sin precio
    @Test
    void registrarPiezaSinPrecio() {

        Pieza pieza = crearPieza();
        pieza.setPrecio(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> piezaServicio.registrar(pieza)
        );

        verify(piezaDAO, never()).guardar(any());
    }

    // Comprueba que el precio de una pieza no pueda ser cero
    @Test
    void registrarPiezaConPrecioCero() {

        Pieza pieza = crearPieza();
        pieza.setPrecio(BigDecimal.ZERO);

        assertThrows(
                IllegalArgumentException.class,
                () -> piezaServicio.registrar(pieza)
        );

        verify(piezaDAO, never()).guardar(any());
    }

    // Comprueba que el precio de una pieza no pueda ser negativo
    @Test
    void registrarPiezaConPrecioNegativo() {

        Pieza pieza = crearPieza();
        pieza.setPrecio(new BigDecimal("-5.00"));

        assertThrows(
                IllegalArgumentException.class,
                () -> piezaServicio.registrar(pieza)
        );

        verify(piezaDAO, never()).guardar(any());
    }

    // Comprueba que no se pueda registrar una pieza duplicada
    @Test
    void registrarPiezaDuplicada() {

        Pieza pieza = crearPieza();

        when(piezaDAO.buscarPorCodigo("PIEZA001"))
                .thenReturn(pieza);

        assertThrows(
                IllegalArgumentException.class,
                () -> piezaServicio.registrar(pieza)
        );

        verify(piezaDAO, never()).guardar(any());
    }

    // Comprueba que se pueda registrar una pieza correctamente
    @Test
    void registrarPiezaCorrectamente() {

        Pieza pieza = crearPieza();

        when(piezaDAO.buscarPorCodigo("PIEZA001"))
                .thenReturn(null);

        assertDoesNotThrow(
                () -> piezaServicio.registrar(pieza)
        );

        verify(piezaDAO, times(1)).guardar(pieza);
    }

    // Comprueba que se pueda registrar una pieza sin descripción
    @Test
    void registrarPiezaSinDescripcion() {

        Pieza pieza = crearPieza();
        pieza.setDescripcion(null);

        when(piezaDAO.buscarPorCodigo("PIEZA001"))
                .thenReturn(null);

        assertDoesNotThrow(
                () -> piezaServicio.registrar(pieza)
        );

        verify(piezaDAO, times(1)).guardar(pieza);
    }

    // Comprueba que se rechace una descripción demasiado larga
    @Test
    void registrarPiezaConDescripcionDemasiadoLarga() {

        Pieza pieza = crearPieza();
        pieza.setDescripcion("A".repeat(501));

        assertThrows(
                IllegalArgumentException.class,
                () -> piezaServicio.registrar(pieza)
        );

        verify(piezaDAO, never()).guardar(any());
    }

    // Comprueba que se pueda buscar una pieza por código
    @Test
    void buscarPiezaPorCodigo() {

        Pieza pieza = crearPieza();

        when(piezaDAO.buscarPorCodigo("PIEZA001"))
                .thenReturn(pieza);

        Pieza resultado = piezaServicio.buscarPorCodigo("PIEZA001");

        assertNotNull(resultado);
        assertEquals("PIEZA001", resultado.getCodigo());

        verify(piezaDAO, times(1)).buscarPorCodigo("PIEZA001");
    }

    // Comprueba que se puedan listar todas las piezas
    @Test
    void listarTodasLasPiezas() {

        List<Pieza> piezas = List.of(crearPieza());

        when(piezaDAO.listarTodas())
                .thenReturn(piezas);

        List<Pieza> resultado = piezaServicio.listarTodas();

        assertEquals(1, resultado.size());

        verify(piezaDAO, times(1)).listarTodas();
    }
}
