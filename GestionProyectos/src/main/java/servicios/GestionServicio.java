
package servicios;

import dao.GestionDAO;
import dao.GestionDAOImpl;
import dao.ProveedorDAO;
import dao.ProveedorDAOImpl;
import dao.PiezaDAO;
import dao.PiezaDAOImpl;
import dao.ProyectoDAO;
import dao.ProyectoDAOImpl;

import modelo.Gestion;
import utilidades.SesionUsuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

public class GestionServicio {

    private static final Logger logger =
            LoggerFactory.getLogger(GestionServicio.class);

    private final GestionDAO gestionDAO;
    private final ProveedorDAO proveedorDAO;
    private final PiezaDAO piezaDAO;
    private final ProyectoDAO proyectoDAO;

    // Constructor que utiliza los DAO predeterminados
    public GestionServicio() {
        this(
                new GestionDAOImpl(),
                new ProveedorDAOImpl(),
                new PiezaDAOImpl(),
                new ProyectoDAOImpl()
        );
    }

    // Constructor que permite inyectar DAO, útil también para las pruebas
    public GestionServicio(
            GestionDAO gestionDAO,
            ProveedorDAO proveedorDAO,
            PiezaDAO piezaDAO,
            ProyectoDAO proyectoDAO
    ) {
        this.gestionDAO = gestionDAO;
        this.proveedorDAO = proveedorDAO;
        this.piezaDAO = piezaDAO;
        this.proyectoDAO = proyectoDAO;
    }

    // Registra una nueva gestión
    public void registrar(Gestion gestion) {

        // Solo los administradores pueden registrar gestiones
        SesionUsuario.exigirAdministrador();

        logger.info("Iniciando registro de gestión.");

        try {

            if (gestion == null) {
                throw new IllegalArgumentException("La gestión no puede ser nula.");
            }

            if (gestion.getFecha() == null) {
                throw new IllegalArgumentException("La fecha es obligatoria.");
            }

            if (gestion.getCantidad() == null || gestion.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
            }

            if (gestion.getProveedor() == null ||
                    gestion.getPieza() == null ||
                    gestion.getProyecto() == null) {

                throw new IllegalArgumentException(
                        "Debe indicarse un proveedor, una pieza y un proyecto."
                );
            }

            if (proveedorDAO.buscarPorCodigo(
                    gestion.getProveedor().getCodigo()) == null) {

                throw new IllegalArgumentException("El proveedor indicado no existe.");
            }

            if (piezaDAO.buscarPorCodigo(
                    gestion.getPieza().getCodigo()) == null) {

                throw new IllegalArgumentException("La pieza indicada no existe.");
            }

            if (proyectoDAO.buscarPorCodigo(
                    gestion.getProyecto().getCodigo()) == null) {

                throw new IllegalArgumentException("El proyecto indicado no existe.");
            }

            gestionDAO.guardar(gestion);

            logger.info("Gestión registrada correctamente.");

        } catch (RuntimeException e) {
            logger.error("Error al registrar la gestión.", e);
            throw e;
        }
    }

    // Busca una gestión por su identificador
    public Gestion buscarPorId(Long id) {

        logger.info("Buscando gestión con identificador: {}", id);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "El identificador debe ser mayor que cero."
            );
        }

        try {
            return gestionDAO.buscarPorId(id);
        } catch (RuntimeException e) {
            logger.error("Error al buscar la gestión.", e);
            throw e;
        }
    }

    // Devuelve todas las gestiones
    public List<Gestion> listarTodas() {

        logger.info("Listando todas las gestiones.");

        try {
            return gestionDAO.listarTodas();
        } catch (RuntimeException e) {
            logger.error("Error al listar las gestiones.", e);
            throw e;
        }
    }

    // Busca gestiones por proveedor
    public List<Gestion> buscarPorProveedor(String codigoProveedor) {

        logger.info("Buscando gestiones del proveedor: {}", codigoProveedor);

        if (codigoProveedor == null || codigoProveedor.isBlank()) {
            throw new IllegalArgumentException(
                    "El código del proveedor es obligatorio."
            );
        }

        try {
            return gestionDAO.buscarPorProveedor(codigoProveedor);
        } catch (RuntimeException e) {
            logger.error("Error al buscar gestiones por proveedor.", e);
            throw e;
        }
    }

    // Busca gestiones por pieza
    public List<Gestion> buscarPorPieza(String codigoPieza) {

        logger.info("Buscando gestiones de la pieza: {}", codigoPieza);

        if (codigoPieza == null || codigoPieza.isBlank()) {
            throw new IllegalArgumentException(
                    "El código de la pieza es obligatorio."
            );
        }

        try {
            return gestionDAO.buscarPorPieza(codigoPieza);
        } catch (RuntimeException e) {
            logger.error("Error al buscar gestiones por pieza.", e);
            throw e;
        }
    }

    // Busca gestiones por proyecto
    public List<Gestion> buscarPorProyecto(String codigoProyecto) {

        logger.info("Buscando gestiones del proyecto: {}", codigoProyecto);

        if (codigoProyecto == null || codigoProyecto.isBlank()) {
            throw new IllegalArgumentException(
                    "El código del proyecto es obligatorio."
            );
        }

        try {
            return gestionDAO.buscarPorProyecto(codigoProyecto);
        } catch (RuntimeException e) {
            logger.error("Error al buscar gestiones por proyecto.", e);
            throw e;
        }
    }

    // Búsqueda avanzada con filtros opcionales
    public List<Gestion> buscarAvanzado(
            String codigoProveedor,
            String codigoPieza,
            String codigoProyecto,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            Integer cantidadMinima,
            Integer cantidadMaxima
    ) {

        logger.info("Iniciando búsqueda avanzada de gestiones.");

        if (fechaDesde != null && fechaHasta != null &&
                fechaDesde.isAfter(fechaHasta)) {

            throw new IllegalArgumentException(
                    "La fecha inicial no puede ser posterior a la fecha final."
            );
        }

        if (cantidadMinima != null && cantidadMaxima != null &&
                cantidadMinima > cantidadMaxima) {

            throw new IllegalArgumentException(
                    "La cantidad mínima no puede superar la cantidad máxima."
            );
        }

        if (cantidadMinima != null && cantidadMinima <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad mínima debe ser mayor que cero."
            );
        }

        if (cantidadMaxima != null && cantidadMaxima <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad máxima debe ser mayor que cero."
            );
        }

        try {

            List<Gestion> resultado = gestionDAO.buscarAvanzado(
                    codigoProveedor,
                    codigoPieza,
                    codigoProyecto,
                    fechaDesde,
                    fechaHasta,
                    cantidadMinima,
                    cantidadMaxima
            );

            logger.info(
                    "Búsqueda avanzada completada. Resultados: {}",
                    resultado.size()
            );

            return resultado;

        } catch (RuntimeException e) {

            logger.error("Error en la búsqueda avanzada.", e);
            throw e;
        }
    }

    // Calcula el coste total de los suministros agrupados por proyecto
    public List<Object[]> calcularCostesPorProyecto() {

        logger.info("Calculando costes de los proyectos.");

        try {

            List<Object[]> resultados = gestionDAO.calcularCostesPorProyecto();

            logger.info(
                    "Cálculo de costes completado. Proyectos encontrados: {}",
                    resultados.size()
            );

            return resultados;

        } catch (RuntimeException e) {

            logger.error("Error al calcular los costes de los proyectos.", e);
            throw e;
        }
    }

    // Calcula la cantidad total de unidades suministradas
    public Long calcularCantidadTotalSuministrada() {

        logger.info("Calculando cantidad total suministrada.");

        try {

            Long total = gestionDAO.calcularCantidadTotalSuministrada();

            logger.info("Cantidad total calculada: {}", total);

            return total;

        } catch (RuntimeException e) {

            logger.error("Error al calcular la cantidad total.", e);
            throw e;
        }
    }

    // Obtiene los proveedores ordenados por cantidad suministrada
    public List<Object[]> obtenerProveedoresMasActivos() {

        logger.info("Consultando proveedores más activos.");

        try {

            List<Object[]> resultados = gestionDAO.obtenerProveedoresMasActivos();

            logger.info("Proveedores encontrados: {}", resultados.size());

            return resultados;

        } catch (RuntimeException e) {

            logger.error("Error al consultar los proveedores más activos.", e);
            throw e;
        }
    }

    // Obtiene las piezas ordenadas por cantidad utilizada
    public List<Object[]> obtenerPiezasMasUtilizadas() {

        logger.info("Consultando piezas más utilizadas.");

        try {

            List<Object[]> resultados = gestionDAO.obtenerPiezasMasUtilizadas();

            logger.info("Piezas encontradas: {}", resultados.size());

            return resultados;

        } catch (RuntimeException e) {

            logger.error("Error al consultar las piezas más utilizadas.", e);
            throw e;
        }
    }

    // Actualiza una gestión existente
    public void actualizar(Gestion gestion) {

        // Solo los administradores pueden actualizar gestiones
        SesionUsuario.exigirAdministrador();

        logger.info("Iniciando actualización de gestión.");

        try {

            if (gestion == null) {
                throw new IllegalArgumentException("La gestión no puede ser nula.");
            }

            if (gestion.getId() == null) {
                throw new IllegalArgumentException(
                        "La gestión debe tener un identificador."
                );
            }

            if (gestion.getFecha() == null) {
                throw new IllegalArgumentException("La fecha es obligatoria.");
            }

            if (gestion.getCantidad() == null || gestion.getCantidad() <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
            }

            if (gestion.getProveedor() == null ||
                    gestion.getPieza() == null ||
                    gestion.getProyecto() == null) {

                throw new IllegalArgumentException(
                        "Debe indicarse un proveedor, una pieza y un proyecto."
                );
            }

            if (gestionDAO.buscarPorId(gestion.getId()) == null) {
                throw new IllegalArgumentException(
                        "No existe ninguna gestión con ese identificador."
                );
            }

            if (proveedorDAO.buscarPorCodigo(
                    gestion.getProveedor().getCodigo()) == null) {

                throw new IllegalArgumentException("El proveedor indicado no existe.");
            }

            if (piezaDAO.buscarPorCodigo(
                    gestion.getPieza().getCodigo()) == null) {

                throw new IllegalArgumentException("La pieza indicada no existe.");
            }

            if (proyectoDAO.buscarPorCodigo(
                    gestion.getProyecto().getCodigo()) == null) {

                throw new IllegalArgumentException("El proyecto indicado no existe.");
            }

            gestionDAO.actualizar(gestion);

            logger.info("Gestión actualizada correctamente.");

        } catch (RuntimeException e) {
            logger.error("Error al actualizar la gestión.", e);
            throw e;
        }
    }

    // Elimina una gestión por su identificador
    public void eliminar(Long id) {

        // Solo los administradores pueden eliminar gestiones
        SesionUsuario.exigirAdministrador();

        logger.info("Iniciando eliminación de gestión: {}", id);

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "El identificador debe ser mayor que cero."
            );
        }

        try {

            if (gestionDAO.buscarPorId(id) == null) {
                throw new IllegalArgumentException(
                        "No existe ninguna gestión con ese identificador."
                );
            }

            gestionDAO.eliminar(id);

            logger.info("Gestión eliminada correctamente.");

        } catch (RuntimeException e) {
            logger.error("Error al eliminar la gestión.", e);
            throw e;
        }
    }
}
