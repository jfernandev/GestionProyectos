
package servicios;

import dao.ProveedorDAO;
import dao.ProveedorDAOImpl;
import modelo.Proveedor;
import utilidades.SesionUsuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProveedorServicio {

    // Logger encargado de registrar las operaciones y los errores
    private static final Logger logger =
            LoggerFactory.getLogger(ProveedorServicio.class);

    // DAO encargado de comunicarse con la base de datos
    private final ProveedorDAO proveedorDAO;

    // Constructor que inicializa el DAO
    public ProveedorServicio() {
        this.proveedorDAO = new ProveedorDAOImpl();
    }

    // Constructor alternativo que permite proporcionar otro DAO
    public ProveedorServicio(ProveedorDAO proveedorDAO) {
        this.proveedorDAO = proveedorDAO;
    }

    // Registra un proveedor después de validar sus datos
    public void registrar(Proveedor proveedor) {

        // Solo los administradores pueden registrar proveedores
        SesionUsuario.exigirAdministrador();

        try {
            validarProveedor(proveedor);

            // Comprobamos que no exista otro proveedor con el mismo código
            if (proveedorDAO.buscarPorCodigo(proveedor.getCodigo()) != null) {
                throw new IllegalArgumentException(
                        "Ya existe un proveedor con ese código."
                );
            }

            proveedorDAO.guardar(proveedor);

            logger.info("Proveedor registrado correctamente. Código: {}",
                    proveedor.getCodigo());

        } catch (RuntimeException e) {
            logger.error("Error al registrar el proveedor.", e);
            throw e;
        }
    }

    // Busca un proveedor por su código
    public Proveedor buscarPorCodigo(String codigo) {

        try {
            validarTexto(codigo, "El código");

            Proveedor proveedor = proveedorDAO.buscarPorCodigo(codigo.trim());

            logger.info("Búsqueda de proveedor por código: {}", codigo.trim());

            return proveedor;

        } catch (RuntimeException e) {
            logger.error("Error al buscar el proveedor por código: {}", codigo, e);
            throw e;
        }
    }

    // Busca proveedores por nombre
    public List<Proveedor> buscarPorNombre(String nombre) {

        try {
            validarTexto(nombre, "El nombre");

            List<Proveedor> proveedores =
                    proveedorDAO.buscarPorNombre(nombre.trim());

            logger.info("Búsqueda de proveedores por nombre realizada. Resultados: {}",
                    proveedores.size());

            return proveedores;

        } catch (RuntimeException e) {
            logger.error("Error al buscar proveedores por nombre.", e);
            throw e;
        }
    }

    // Busca proveedores por dirección
    public List<Proveedor> buscarPorDireccion(String direccion) {

        try {
            validarTexto(direccion, "La dirección");

            List<Proveedor> proveedores =
                    proveedorDAO.buscarPorDireccion(direccion.trim());

            logger.info("Búsqueda de proveedores por dirección realizada. Resultados: {}",
                    proveedores.size());

            return proveedores;

        } catch (RuntimeException e) {
            logger.error("Error al buscar proveedores por dirección.", e);
            throw e;
        }
    }

    // Devuelve todos los proveedores
    public List<Proveedor> listarTodos() {

        try {
            List<Proveedor> proveedores = proveedorDAO.listarTodos();

            logger.info("Listado de proveedores realizado. Total: {}",
                    proveedores.size());

            return proveedores;

        } catch (RuntimeException e) {
            logger.error("Error al listar los proveedores.", e);
            throw e;
        }
    }

    // Actualiza un proveedor existente
    public void actualizar(Proveedor proveedor) {

        // Solo los administradores pueden actualizar proveedores
        SesionUsuario.exigirAdministrador();

        try {
            validarProveedor(proveedor);

            // Comprobamos que el proveedor existe
            if (proveedorDAO.buscarPorCodigo(proveedor.getCodigo()) == null) {
                throw new IllegalArgumentException(
                        "No existe ningún proveedor con ese código."
                );
            }

            proveedorDAO.actualizar(proveedor);

            logger.info("Proveedor actualizado correctamente. Código: {}",
                    proveedor.getCodigo());

        } catch (RuntimeException e) {
            logger.error("Error al actualizar el proveedor.", e);
            throw e;
        }
    }

    // Elimina un proveedor por su código
    public void eliminar(String codigo) {

        // Solo los administradores pueden eliminar proveedores
        SesionUsuario.exigirAdministrador();

        try {
            validarTexto(codigo, "El código");

            // Comprobamos que el proveedor existe
            if (proveedorDAO.buscarPorCodigo(codigo.trim()) == null) {
                throw new IllegalArgumentException(
                        "No existe ningún proveedor con ese código."
                );
            }

            proveedorDAO.eliminar(codigo.trim());

            logger.info("Proveedor eliminado correctamente. Código: {}",
                    codigo.trim());

        } catch (RuntimeException e) {
            logger.error("Error al eliminar el proveedor. Código: {}", codigo, e);
            throw e;
        }
    }

    // Comprueba que todos los datos obligatorios del proveedor sean correctos
    private void validarProveedor(Proveedor proveedor) {

        if (proveedor == null) {
            throw new IllegalArgumentException(
                    "El proveedor no puede ser nulo."
            );
        }

        validarCampo(proveedor.getCodigo(), "El código", 20);
        validarCampo(proveedor.getNombre(), "El nombre", 100);
        validarCampo(proveedor.getApellidos(), "Los apellidos", 150);
        validarCampo(proveedor.getDireccion(), "La dirección", 200);
    }

    // Valida un campo obligatorio y su longitud máxima
    private void validarCampo(String valor, String campo, int longitudMaxima) {

        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    campo + " es obligatorio."
            );
        }

        if (valor.trim().length() > longitudMaxima) {
            throw new IllegalArgumentException(
                    campo + " no puede superar los " +
                            longitudMaxima + " caracteres."
            );
        }
    }

    // Valida que un texto de búsqueda no esté vacío
    private void validarTexto(String valor, String campo) {

        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    campo + " no puede estar vacío."
            );
        }
    }
}
