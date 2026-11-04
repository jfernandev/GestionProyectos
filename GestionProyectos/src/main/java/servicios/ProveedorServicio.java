
package servicios;

import dao.ProveedorDAO;
import dao.ProveedorDAOImpl;
import modelo.Proveedor;

import java.util.List;

public class ProveedorServicio {

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

        validarProveedor(proveedor);

        // Comprobamos que no exista otro proveedor con el mismo código
        if (proveedorDAO.buscarPorCodigo(proveedor.getCodigo()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un proveedor con ese código."
            );
        }

        proveedorDAO.guardar(proveedor);
    }

    // Busca un proveedor por su código
    public Proveedor buscarPorCodigo(String codigo) {

        validarTexto(codigo, "El código");

        return proveedorDAO.buscarPorCodigo(codigo.trim());
    }

    // Busca proveedores por nombre
    public List<Proveedor> buscarPorNombre(String nombre) {

        validarTexto(nombre, "El nombre");

        return proveedorDAO.buscarPorNombre(nombre.trim());
    }

    // Busca proveedores por dirección
    public List<Proveedor> buscarPorDireccion(String direccion) {

        validarTexto(direccion, "La dirección");

        return proveedorDAO.buscarPorDireccion(direccion.trim());
    }

    // Devuelve todos los proveedores
    public List<Proveedor> listarTodos() {
        return proveedorDAO.listarTodos();
    }

    // Actualiza un proveedor existente
    public void actualizar(Proveedor proveedor) {

        validarProveedor(proveedor);

        // Comprobamos que el proveedor existe
        if (proveedorDAO.buscarPorCodigo(proveedor.getCodigo()) == null) {
            throw new IllegalArgumentException(
                    "No existe ningún proveedor con ese código."
            );
        }

        proveedorDAO.actualizar(proveedor);
    }

    // Elimina un proveedor por su código
    public void eliminar(String codigo) {

        validarTexto(codigo, "El código");

        // Comprobamos que el proveedor existe
        if (proveedorDAO.buscarPorCodigo(codigo.trim()) == null) {
            throw new IllegalArgumentException(
                    "No existe ningún proveedor con ese código."
            );
        }

        proveedorDAO.eliminar(codigo.trim());
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
