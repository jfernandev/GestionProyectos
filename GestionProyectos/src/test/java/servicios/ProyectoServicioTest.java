
package servicios;

import dao.ProyectoDAO;
import modelo.Proyecto;
import modelo.Usuario;
import utilidades.SesionUsuario;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProyectoServicioTest {

    // DAO simulado para evitar acceder a MariaDB
    private ProyectoDAO proyectoDAO;

    // Servicio que vamos a probar
    private ProyectoServicio proyectoServicio;

    // Se ejecuta antes de cada prueba
    @BeforeEach
    void setUp() {

        proyectoDAO = mock(ProyectoDAO.class);

        // Creamos un administrador simulado
        Usuario administrador = new Usuario(
                "admin",
                "hash_simulado",
                "ADMIN"
        );

        // Establecemos la sesión del administrador
        SesionUsuario.establecerUsuario(administrador);

        // Inyectamos el DAO simulado
        proyectoServicio = new ProyectoServicio(proyectoDAO);
    }

    // Se ejecuta después de cada prueba
    @AfterEach
    void tearDown() {

        // Cerramos la sesión para evitar que afecte a otras pruebas
        SesionUsuario.cerrarSesion();
    }

    // Crea un proyecto con datos válidos
    private Proyecto crearProyecto() {

        Proyecto proyecto = new Proyecto();

        proyecto.setCodigo("PROY001");
        proyecto.setNombre("Construcción edificio");
        proyecto.setCiudad("Vitoria-Gasteiz");

        return proyecto;
    }

    // Comprueba que no se pueda registrar un proyecto nulo
    @Test
    void registrarProyectoNulo() {

        assertThrows(
                IllegalArgumentException.class,
                () -> proyectoServicio.registrar(null)
        );

        verify(proyectoDAO, never()).guardar(any());
    }

    // Comprueba que no se pueda registrar un proyecto sin código
    @Test
    void registrarProyectoSinCodigo() {

        Proyecto proyecto = crearProyecto();
        proyecto.setCodigo("");

        assertThrows(
                IllegalArgumentException.class,
                () -> proyectoServicio.registrar(proyecto)
        );

        verify(proyectoDAO, never()).guardar(any());
    }

    // Comprueba que no se pueda registrar un proyecto sin nombre
    @Test
    void registrarProyectoSinNombre() {

        Proyecto proyecto = crearProyecto();
        proyecto.setNombre("");

        assertThrows(
                IllegalArgumentException.class,
                () -> proyectoServicio.registrar(proyecto)
        );

        verify(proyectoDAO, never()).guardar(any());
    }

    // Comprueba que no se pueda registrar un proyecto sin ciudad
    @Test
    void registrarProyectoSinCiudad() {

        Proyecto proyecto = crearProyecto();
        proyecto.setCiudad("");

        assertThrows(
                IllegalArgumentException.class,
                () -> proyectoServicio.registrar(proyecto)
        );

        verify(proyectoDAO, never()).guardar(any());
    }

    // Comprueba que no se pueda registrar un proyecto duplicado
    @Test
    void registrarProyectoDuplicado() {

        Proyecto proyecto = crearProyecto();

        when(proyectoDAO.buscarPorCodigo("PROY001"))
                .thenReturn(proyecto);

        assertThrows(
                IllegalArgumentException.class,
                () -> proyectoServicio.registrar(proyecto)
        );

        verify(proyectoDAO, never()).guardar(any());
    }

    // Comprueba que se pueda registrar un proyecto correctamente
    @Test
    void registrarProyectoCorrectamente() {

        Proyecto proyecto = crearProyecto();

        when(proyectoDAO.buscarPorCodigo("PROY001"))
                .thenReturn(null);

        assertDoesNotThrow(
                () -> proyectoServicio.registrar(proyecto)
        );

        verify(proyectoDAO, times(1)).guardar(proyecto);
    }

    // Comprueba que se pueda buscar un proyecto por código
    @Test
    void buscarProyectoPorCodigo() {

        Proyecto proyecto = crearProyecto();

        when(proyectoDAO.buscarPorCodigo("PROY001"))
                .thenReturn(proyecto);

        Proyecto resultado =
                proyectoServicio.buscarPorCodigo("PROY001");

        assertNotNull(resultado);
        assertEquals("PROY001", resultado.getCodigo());

        verify(proyectoDAO, times(1)).buscarPorCodigo("PROY001");
    }

    // Comprueba que se puedan buscar proyectos por nombre
    @Test
    void buscarProyectoPorNombre() {

        List<Proyecto> proyectos = List.of(crearProyecto());

        when(proyectoDAO.buscarPorNombre("Construcción edificio"))
                .thenReturn(proyectos);

        List<Proyecto> resultado =
                proyectoServicio.buscarPorNombre("Construcción edificio");

        assertEquals(1, resultado.size());

        verify(proyectoDAO, times(1))
                .buscarPorNombre("Construcción edificio");
    }

    // Comprueba que se puedan buscar proyectos por ciudad
    @Test
    void buscarProyectoPorCiudad() {

        List<Proyecto> proyectos = List.of(crearProyecto());

        when(proyectoDAO.buscarPorCiudad("Vitoria-Gasteiz"))
                .thenReturn(proyectos);

        List<Proyecto> resultado =
                proyectoServicio.buscarPorCiudad("Vitoria-Gasteiz");

        assertEquals(1, resultado.size());

        verify(proyectoDAO, times(1))
                .buscarPorCiudad("Vitoria-Gasteiz");
    }

    // Comprueba que se puedan listar todos los proyectos
    @Test
    void listarTodosLosProyectos() {

        List<Proyecto> proyectos = List.of(crearProyecto());

        when(proyectoDAO.listarTodos())
                .thenReturn(proyectos);

        List<Proyecto> resultado = proyectoServicio.listarTodos();

        assertEquals(1, resultado.size());

        verify(proyectoDAO, times(1)).listarTodos();
    }

    // Comprueba que no se pueda actualizar un proyecto inexistente
    @Test
    void actualizarProyectoInexistente() {

        Proyecto proyecto = crearProyecto();

        when(proyectoDAO.buscarPorCodigo("PROY001"))
                .thenReturn(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> proyectoServicio.actualizar(proyecto)
        );

        verify(proyectoDAO, never()).actualizar(any());
    }

    // Comprueba que se pueda actualizar un proyecto correctamente
    @Test
    void actualizarProyectoCorrectamente() {

        Proyecto proyecto = crearProyecto();

        when(proyectoDAO.buscarPorCodigo("PROY001"))
                .thenReturn(proyecto);

        assertDoesNotThrow(
                () -> proyectoServicio.actualizar(proyecto)
        );

        verify(proyectoDAO, times(1)).actualizar(proyecto);
    }

    // Comprueba que no se pueda eliminar un proyecto inexistente
    @Test
    void eliminarProyectoInexistente() {

        when(proyectoDAO.buscarPorCodigo("PROY999"))
                .thenReturn(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> proyectoServicio.eliminar("PROY999")
        );

        verify(proyectoDAO, never()).eliminar(anyString());
    }

    // Comprueba que se pueda eliminar un proyecto correctamente
    @Test
    void eliminarProyectoCorrectamente() {

        Proyecto proyecto = crearProyecto();

        when(proyectoDAO.buscarPorCodigo("PROY001"))
                .thenReturn(proyecto);

        assertDoesNotThrow(
                () -> proyectoServicio.eliminar("PROY001")
        );

        verify(proyectoDAO, times(1)).eliminar("PROY001");
    }
}
