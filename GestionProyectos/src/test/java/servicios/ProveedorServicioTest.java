
package servicios;

import dao.ProveedorDAO;
import modelo.Proveedor;
import modelo.Usuario;
import utilidades.SesionUsuario;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProveedorServicioTest {

    // DAO simulado: no accederemos a MariaDB
    private ProveedorDAO proveedorDAO;

    // Servicio que queremos probar
    private ProveedorServicio proveedorServicio;

    // Se ejecuta antes de cada prueba
    @BeforeEach
    void setUp() {

        proveedorDAO = mock(ProveedorDAO.class);

        // Creamos un administrador simulado
        Usuario administrador = new Usuario(
                "admin",
                "hash_simulado",
                "ADMIN"
        );

        // Establecemos la sesión del administrador
        SesionUsuario.establecerUsuario(administrador);

        // Inyectamos el DAO simulado en el servicio
        proveedorServicio = new ProveedorServicio(proveedorDAO);
    }

    // Se ejecuta después de cada prueba
    @AfterEach
    void tearDown() {

        // Cerramos la sesión para evitar que afecte a otras pruebas
        SesionUsuario.cerrarSesion();
    }

    // Crea un proveedor válido para utilizarlo en las pruebas
    private Proveedor crearProveedor() {

        Proveedor proveedor = new Proveedor();

        proveedor.setCodigo("PROV001");
        proveedor.setNombre("Ángel");
        proveedor.setApellidos("Cornés de la Vera");
        proveedor.setDireccion("Calle Baiona 24");

        return proveedor;
    }

    // Comprueba que no se pueda registrar un proveedor nulo
    @Test
    void registrarProveedorNulo() {

        assertThrows(
                IllegalArgumentException.class,
                () -> proveedorServicio.registrar(null)
        );

        // No debe intentar guardar nada
        verify(proveedorDAO, never()).guardar(any());
    }

    // Comprueba que no se pueda registrar un proveedor sin código
    @Test
    void registrarProveedorSinCodigo() {

        Proveedor proveedor = crearProveedor();
        proveedor.setCodigo("");

        assertThrows(
                IllegalArgumentException.class,
                () -> proveedorServicio.registrar(proveedor)
        );

        // No debe acceder al DAO
        verify(proveedorDAO, never()).guardar(any());
    }

    // Comprueba que no se puedan registrar códigos duplicados
    @Test
    void registrarProveedorDuplicado() {

        Proveedor proveedor = crearProveedor();

        // Simulamos que el proveedor ya existe
        when(proveedorDAO.buscarPorCodigo("PROV001"))
                .thenReturn(proveedor);

        assertThrows(
                IllegalArgumentException.class,
                () -> proveedorServicio.registrar(proveedor)
        );

        // Comprobamos que no se haya guardado
        verify(proveedorDAO, never()).guardar(any());
    }

    // Comprueba que se pueda registrar correctamente un proveedor
    @Test
    void registrarProveedorCorrectamente() {

        Proveedor proveedor = crearProveedor();

        // Simulamos que todavía no existe
        when(proveedorDAO.buscarPorCodigo("PROV001"))
                .thenReturn(null);

        // Ejecutamos el registro
        assertDoesNotThrow(
                () -> proveedorServicio.registrar(proveedor)
        );

        // Comprobamos que se haya llamado al método guardar
        verify(proveedorDAO, times(1)).guardar(proveedor);
    }

    // Comprueba que no se pueda buscar con un código vacío
    @Test
    void buscarProveedorConCodigoVacio() {

        assertThrows(
                IllegalArgumentException.class,
                () -> proveedorServicio.buscarPorCodigo("")
        );

        verify(proveedorDAO, never()).buscarPorCodigo(anyString());
    }

    // Comprueba que no se pueda actualizar un proveedor inexistente
    @Test
    void actualizarProveedorInexistente() {

        Proveedor proveedor = crearProveedor();

        // Simulamos que el proveedor no existe
        when(proveedorDAO.buscarPorCodigo("PROV001"))
                .thenReturn(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> proveedorServicio.actualizar(proveedor)
        );

        // No debe intentar actualizarlo
        verify(proveedorDAO, never()).actualizar(any());
    }

    // Comprueba que se pueda actualizar un proveedor existente
    @Test
    void actualizarProveedorCorrectamente() {

        Proveedor proveedor = crearProveedor();

        // Simulamos que el proveedor existe
        when(proveedorDAO.buscarPorCodigo("PROV001"))
                .thenReturn(proveedor);

        assertDoesNotThrow(
                () -> proveedorServicio.actualizar(proveedor)
        );

        verify(proveedorDAO, times(1)).actualizar(proveedor);
    }

    // Comprueba que no se pueda eliminar un proveedor inexistente
    @Test
    void eliminarProveedorInexistente() {

        when(proveedorDAO.buscarPorCodigo("PROV999"))
                .thenReturn(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> proveedorServicio.eliminar("PROV999")
        );

        verify(proveedorDAO, never()).eliminar(anyString());
    }

    // Comprueba que se pueda eliminar un proveedor existente
    @Test
    void eliminarProveedorCorrectamente() {

        Proveedor proveedor = crearProveedor();

        when(proveedorDAO.buscarPorCodigo("PROV001"))
                .thenReturn(proveedor);

        assertDoesNotThrow(
                () -> proveedorServicio.eliminar("PROV001")
        );

        verify(proveedorDAO, times(1)).eliminar("PROV001");
    }

    // Comprueba la búsqueda de proveedores por nombre
    @Test
    void buscarProveedorPorNombre() {

        List<Proveedor> proveedores = List.of(crearProveedor());

        when(proveedorDAO.buscarPorNombre("Ángel"))
                .thenReturn(proveedores);

        List<Proveedor> resultado =
                proveedorServicio.buscarPorNombre("Ángel");

        assertEquals(1, resultado.size());
        assertEquals("Ángel", resultado.get(0).getNombre());

        verify(proveedorDAO, times(1)).buscarPorNombre("Ángel");
    }

    // Comprueba que se puedan listar todos los proveedores
    @Test
    void listarTodosLosProveedores() {

        List<Proveedor> proveedores = List.of(crearProveedor());

        when(proveedorDAO.listarTodos())
                .thenReturn(proveedores);

        List<Proveedor> resultado = proveedorServicio.listarTodos();

        assertEquals(1, resultado.size());

        verify(proveedorDAO, times(1)).listarTodos();
    }
}
