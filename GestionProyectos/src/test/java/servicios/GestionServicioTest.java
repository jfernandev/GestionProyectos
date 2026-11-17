
package servicios;

import dao.GestionDAO;
import dao.ProveedorDAO;
import dao.PiezaDAO;
import dao.ProyectoDAO;

import modelo.Gestion;
import modelo.Proveedor;
import modelo.Pieza;
import modelo.Proyecto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GestionServicioTest {

    // DAOs simulados para no acceder a la base de datos
    private GestionDAO gestionDAO;
    private ProveedorDAO proveedorDAO;
    private PiezaDAO piezaDAO;
    private ProyectoDAO proyectoDAO;

    // Servicio que vamos a probar
    private GestionServicio gestionServicio;

    // Se ejecuta antes de cada prueba
    @BeforeEach
    void setUp() {

        gestionDAO = mock(GestionDAO.class);
        proveedorDAO = mock(ProveedorDAO.class);
        piezaDAO = mock(PiezaDAO.class);
        proyectoDAO = mock(ProyectoDAO.class);

        // Inyectamos los cuatro DAO simulados
        gestionServicio = new GestionServicio(
                gestionDAO,
                proveedorDAO,
                piezaDAO,
                proyectoDAO
        );
    }

    // Crea un proveedor válido para las pruebas
    private Proveedor crearProveedor() {

        Proveedor proveedor = new Proveedor();

        proveedor.setCodigo("PROV001");
        proveedor.setNombre("Ángel");
        proveedor.setApellidos("Cornés de la Vera");
        proveedor.setDireccion("Calle Baiona 24");

        return proveedor;
    }

    // Crea una pieza válida para las pruebas
    private Pieza crearPieza() {

        Pieza pieza = new Pieza();

        pieza.setCodigo("PIEZA001");
        pieza.setNombre("Tornillo M8");

        return pieza;
    }

    // Crea un proyecto válido para las pruebas
    private Proyecto crearProyecto() {

        Proyecto proyecto = new Proyecto();

        proyecto.setCodigo("PROY001");
        proyecto.setNombre("Construcción edificio");
        proyecto.setCiudad("Vitoria-Gasteiz");

        return proyecto;
    }

    // Crea una gestión con todos sus datos correctos
    private Gestion crearGestion() {

        Gestion gestion = new Gestion();

        gestion.setFecha(LocalDate.now());
        gestion.setCantidad(10);
        gestion.setProveedor(crearProveedor());
        gestion.setPieza(crearPieza());
        gestion.setProyecto(crearProyecto());

        return gestion;
    }

    // Configura las respuestas de los DAO para validar las relaciones
    private void configurarRelacionesExistentes() {

        when(proveedorDAO.buscarPorCodigo("PROV001"))
                .thenReturn(crearProveedor());

        when(piezaDAO.buscarPorCodigo("PIEZA001"))
                .thenReturn(crearPieza());

        when(proyectoDAO.buscarPorCodigo("PROY001"))
                .thenReturn(crearProyecto());
    }

    // Comprueba que no se pueda registrar una gestión nula
    @Test
    void registrarGestionNula() {

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.registrar(null)
        );

        verify(gestionDAO, never()).guardar(any());
    }

    // Comprueba que no se pueda registrar una gestión sin fecha
    @Test
    void registrarGestionSinFecha() {

        Gestion gestion = crearGestion();
        gestion.setFecha(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.registrar(gestion)
        );

        verify(gestionDAO, never()).guardar(any());
    }

    // Comprueba que no se pueda registrar una gestión sin cantidad
    @Test
    void registrarGestionSinCantidad() {

        Gestion gestion = crearGestion();
        gestion.setCantidad(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.registrar(gestion)
        );

        verify(gestionDAO, never()).guardar(any());
    }

    // Comprueba que no se pueda registrar una gestión con cantidad cero
    @Test
    void registrarGestionConCantidadCero() {

        Gestion gestion = crearGestion();
        gestion.setCantidad(0);

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.registrar(gestion)
        );

        verify(gestionDAO, never()).guardar(any());
    }

    // Comprueba que no se pueda registrar una gestión con cantidad negativa
    @Test
    void registrarGestionConCantidadNegativa() {

        Gestion gestion = crearGestion();
        gestion.setCantidad(-5);

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.registrar(gestion)
        );

        verify(gestionDAO, never()).guardar(any());
    }

    // Comprueba que no se pueda registrar una gestión sin proveedor
    @Test
    void registrarGestionSinProveedor() {

        Gestion gestion = crearGestion();
        gestion.setProveedor(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.registrar(gestion)
        );

        verify(gestionDAO, never()).guardar(any());
    }

    // Comprueba que no se pueda registrar una gestión sin pieza
    @Test
    void registrarGestionSinPieza() {

        Gestion gestion = crearGestion();
        gestion.setPieza(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.registrar(gestion)
        );

        verify(gestionDAO, never()).guardar(any());
    }

    // Comprueba que no se pueda registrar una gestión sin proyecto
    @Test
    void registrarGestionSinProyecto() {

        Gestion gestion = crearGestion();
        gestion.setProyecto(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.registrar(gestion)
        );

        verify(gestionDAO, never()).guardar(any());
    }

    // Comprueba que no se pueda registrar una gestión con un proveedor inexistente
    @Test
    void registrarGestionConProveedorInexistente() {

        Gestion gestion = crearGestion();

        when(proveedorDAO.buscarPorCodigo("PROV001"))
                .thenReturn(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.registrar(gestion)
        );

        verify(gestionDAO, never()).guardar(any());
    }

    // Comprueba que se pueda registrar una gestión correctamente
    @Test
    void registrarGestionCorrectamente() {

        Gestion gestion = crearGestion();

        configurarRelacionesExistentes();

        assertDoesNotThrow(
                () -> gestionServicio.registrar(gestion)
        );

        verify(gestionDAO, times(1)).guardar(gestion);
    }

    // Comprueba que se puedan listar todas las gestiones
    @Test
    void listarTodasLasGestiones() {

        List<Gestion> gestiones = List.of(crearGestion());

        when(gestionDAO.listarTodas())
                .thenReturn(gestiones);

        List<Gestion> resultado = gestionServicio.listarTodas();

        assertEquals(1, resultado.size());

        verify(gestionDAO, times(1)).listarTodas();
    }

    // Comprueba que se pueda buscar una gestión por su identificador
    @Test
    void buscarGestionPorId() {

        Gestion gestion = crearGestion();

        when(gestionDAO.buscarPorId(1L))
                .thenReturn(gestion);

        Gestion resultado = gestionServicio.buscarPorId(1L);

        assertNotNull(resultado);

        verify(gestionDAO, times(1)).buscarPorId(1L);
    }

    // Comprueba que se pueda realizar una búsqueda avanzada correctamente
    @Test
    void buscarAvanzadoCorrectamente() {

        List<Gestion> gestiones = List.of(crearGestion());

        LocalDate fechaDesde = LocalDate.of(2026, 1, 1);
        LocalDate fechaHasta = LocalDate.of(2026, 12, 31);

        when(gestionDAO.buscarAvanzado(
                "PROV001",
                "PIEZA001",
                "PROY001",
                fechaDesde,
                fechaHasta,
                5,
                100
        )).thenReturn(gestiones);

        List<Gestion> resultado = gestionServicio.buscarAvanzado(
                "PROV001",
                "PIEZA001",
                "PROY001",
                fechaDesde,
                fechaHasta,
                5,
                100
        );

        assertEquals(1, resultado.size());

        verify(gestionDAO, times(1)).buscarAvanzado(
                "PROV001",
                "PIEZA001",
                "PROY001",
                fechaDesde,
                fechaHasta,
                5,
                100
        );
    }

    // Comprueba que la fecha inicial no pueda ser posterior a la final
    @Test
    void buscarAvanzadoConFechasIncorrectas() {

        LocalDate fechaDesde = LocalDate.of(2026, 12, 31);
        LocalDate fechaHasta = LocalDate.of(2026, 1, 1);

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.buscarAvanzado(
                        null,
                        null,
                        null,
                        fechaDesde,
                        fechaHasta,
                        null,
                        null
                )
        );

        verify(gestionDAO, never()).buscarAvanzado(
                any(), any(), any(), any(), any(), any(), any()
        );
    }

    // Comprueba que la cantidad mínima no pueda superar a la máxima
    @Test
    void buscarAvanzadoConCantidadesIncorrectas() {

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.buscarAvanzado(
                        null,
                        null,
                        null,
                        null,
                        null,
                        100,
                        10
                )
        );

        verify(gestionDAO, never()).buscarAvanzado(
                any(), any(), any(), any(), any(), any(), any()
        );
    }

    // Comprueba que la cantidad mínima no pueda ser cero o negativa
    @Test
    void buscarAvanzadoConCantidadMinimaNoValida() {

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.buscarAvanzado(
                        null,
                        null,
                        null,
                        null,
                        null,
                        0,
                        null
                )
        );

        verify(gestionDAO, never()).buscarAvanzado(
                any(), any(), any(), any(), any(), any(), any()
        );
    }

    // Comprueba que la cantidad máxima no pueda ser cero o negativa
    @Test
    void buscarAvanzadoConCantidadMaximaNoValida() {

        assertThrows(
                IllegalArgumentException.class,
                () -> gestionServicio.buscarAvanzado(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        -5
                )
        );

        verify(gestionDAO, never()).buscarAvanzado(
                any(), any(), any(), any(), any(), any(), any()
        );
    }
}
