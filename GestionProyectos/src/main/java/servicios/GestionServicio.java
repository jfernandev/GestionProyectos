
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
import modelo.Proveedor;
import modelo.Pieza;
import modelo.Proyecto;

import java.time.LocalDate;
import java.util.List;

public class GestionServicio {

    // DAO encargado de las operaciones sobre gestiones
    private final GestionDAO gestionDAO;

    // DAO necesarios para comprobar las entidades relacionadas
    private final ProveedorDAO proveedorDAO;
    private final PiezaDAO piezaDAO;
    private final ProyectoDAO proyectoDAO;

    // Constructor que inicializa todos los DAO
    public GestionServicio() {
        this.gestionDAO = new GestionDAOImpl();
        this.proveedorDAO = new ProveedorDAOImpl();
        this.piezaDAO = new PiezaDAOImpl();
        this.proyectoDAO = new ProyectoDAOImpl();
    }

    // Constructor alternativo para facilitar las pruebas
    public GestionServicio(
            GestionDAO gestionDAO,
            ProveedorDAO proveedorDAO,
            PiezaDAO piezaDAO,
            ProyectoDAO proyectoDAO) {

        this.gestionDAO = gestionDAO;
        this.proveedorDAO = proveedorDAO;
        this.piezaDAO = piezaDAO;
        this.proyectoDAO = proyectoDAO;
    }

    // Registra una nueva gestión
    public void registrar(Gestion gestion) {

        // Validamos los datos básicos
        validarGestion(gestion);

        // Comprobamos que las entidades relacionadas existen
        validarRelaciones(gestion);

        // Guardamos la gestión
        gestionDAO.guardar(gestion);
    }

    // Busca una gestión por su identificador
    public Gestion buscarPorId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "El identificador debe ser un número positivo."
            );
        }

        return gestionDAO.buscarPorId(id);
    }

    // Devuelve todas las gestiones
    public List<Gestion> listarTodas() {
        return gestionDAO.listarTodas();
    }

    // Busca las gestiones de un proveedor
    public List<Gestion> buscarPorProveedor(String codigoProveedor) {

        validarTexto(codigoProveedor, "El código del proveedor");

        // Comprobamos que el proveedor exista
        if (proveedorDAO.buscarPorCodigo(codigoProveedor.trim()) == null) {
            throw new IllegalArgumentException(
                    "No existe ningún proveedor con ese código."
            );
        }

        return gestionDAO.buscarPorProveedor(codigoProveedor.trim());
    }

    // Busca las gestiones correspondientes a una pieza
    public List<Gestion> buscarPorPieza(String codigoPieza) {

        validarTexto(codigoPieza, "El código de la pieza");

        // Comprobamos que la pieza exista
        if (piezaDAO.buscarPorCodigo(codigoPieza.trim()) == null) {
            throw new IllegalArgumentException(
                    "No existe ninguna pieza con ese código."
            );
        }

        return gestionDAO.buscarPorPieza(codigoPieza.trim());
    }

    // Busca las gestiones correspondientes a un proyecto
    public List<Gestion> buscarPorProyecto(String codigoProyecto) {

        validarTexto(codigoProyecto, "El código del proyecto");

        // Comprobamos que el proyecto exista
        if (proyectoDAO.buscarPorCodigo(codigoProyecto.trim()) == null) {
            throw new IllegalArgumentException(
                    "No existe ningún proyecto con ese código."
            );
        }

        return gestionDAO.buscarPorProyecto(codigoProyecto.trim());
    }

    // Actualiza una gestión existente
    public void actualizar(Gestion gestion) {

        validarGestion(gestion);

        if (gestion.getId() == null || gestion.getId() <= 0) {
            throw new IllegalArgumentException(
                    "La gestión debe tener un identificador válido."
            );
        }

        // Comprobamos que la gestión exista
        if (gestionDAO.buscarPorId(gestion.getId()) == null) {
            throw new IllegalArgumentException(
                    "No existe ninguna gestión con ese identificador."
            );
        }

        // Comprobamos las entidades relacionadas
        validarRelaciones(gestion);

        gestionDAO.actualizar(gestion);
    }

    // Elimina una gestión por su identificador
    public void eliminar(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "El identificador debe ser un número positivo."
            );
        }

        // Comprobamos que la gestión exista
        if (gestionDAO.buscarPorId(id) == null) {
            throw new IllegalArgumentException(
                    "No existe ninguna gestión con ese identificador."
            );
        }

        gestionDAO.eliminar(id);
    }

    // Valida los datos básicos de una gestión
    private void validarGestion(Gestion gestion) {

        if (gestion == null) {
            throw new IllegalArgumentException(
                    "La gestión no puede ser nula."
            );
        }

        // La fecha es obligatoria
        LocalDate fecha = gestion.getFecha();

        if (fecha == null) {
            throw new IllegalArgumentException(
                    "La fecha de la gestión es obligatoria."
            );
        }

        // La cantidad debe ser positiva
        if (gestion.getCantidad() == null ||
                gestion.getCantidad() <= 0) {

            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }
    }

    // Comprueba que el proveedor, la pieza y el proyecto existan
    private void validarRelaciones(Gestion gestion) {

        if (gestion.getProveedor() == null ||
                gestion.getProveedor().getCodigo() == null ||
                proveedorDAO.buscarPorCodigo(
                        gestion.getProveedor().getCodigo()
                ) == null) {

            throw new IllegalArgumentException(
                    "El proveedor indicado no existe."
            );
        }

        if (gestion.getPieza() == null ||
                gestion.getPieza().getCodigo() == null ||
                piezaDAO.buscarPorCodigo(
                        gestion.getPieza().getCodigo()
                ) == null) {

            throw new IllegalArgumentException(
                    "La pieza indicada no existe."
            );
        }

        if (gestion.getProyecto() == null ||
                gestion.getProyecto().getCodigo() == null ||
                proyectoDAO.buscarPorCodigo(
                        gestion.getProyecto().getCodigo()
                ) == null) {

            throw new IllegalArgumentException(
                    "El proyecto indicado no existe."
            );
        }
    }

    // Comprueba que un texto obligatorio no esté vacío
    private void validarTexto(String valor, String campo) {

        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    campo + " no puede estar vacío."
            );
        }
    }
}
