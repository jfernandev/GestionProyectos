
package servicios;

import dao.PiezaDAO;
import dao.PiezaDAOImpl;
import modelo.Pieza;

import java.math.BigDecimal;
import java.util.List;

public class PiezaServicio {

    // DAO encargado de comunicarse con la base de datos
    private final PiezaDAO piezaDAO;

    // Constructor que inicializa el DAO
    public PiezaServicio() {
        this.piezaDAO = new PiezaDAOImpl();
    }

    // Constructor alternativo para facilitar las pruebas
    public PiezaServicio(PiezaDAO piezaDAO) {
        this.piezaDAO = piezaDAO;
    }

    // Registra una pieza después de validar sus datos
    public void registrar(Pieza pieza) {

        validarPieza(pieza);

        // Comprobamos que no exista otra pieza con el mismo código
        if (piezaDAO.buscarPorCodigo(pieza.getCodigo()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe una pieza con ese código."
            );
        }

        piezaDAO.guardar(pieza);
    }

    // Busca una pieza por su código
    public Pieza buscarPorCodigo(String codigo) {

        validarTexto(codigo, "El código");

        return piezaDAO.buscarPorCodigo(codigo.trim());
    }

    // Busca piezas por su nombre
    public List<Pieza> buscarPorNombre(String nombre) {

        validarTexto(nombre, "El nombre");

        return piezaDAO.buscarPorNombre(nombre.trim());
    }

    // Devuelve todas las piezas registradas
    public List<Pieza> listarTodas() {
        return piezaDAO.listarTodas();
    }

    // Actualiza una pieza existente
    public void actualizar(Pieza pieza) {

        validarPieza(pieza);

        // Comprobamos que la pieza exista
        if (piezaDAO.buscarPorCodigo(pieza.getCodigo()) == null) {
            throw new IllegalArgumentException(
                    "No existe ninguna pieza con ese código."
            );
        }

        piezaDAO.actualizar(pieza);
    }

    // Elimina una pieza por su código
    public void eliminar(String codigo) {

        validarTexto(codigo, "El código");

        // Comprobamos que la pieza exista
        if (piezaDAO.buscarPorCodigo(codigo.trim()) == null) {
            throw new IllegalArgumentException(
                    "No existe ninguna pieza con ese código."
            );
        }

        piezaDAO.eliminar(codigo.trim());
    }

    // Valida todos los campos de una pieza
    private void validarPieza(Pieza pieza) {

        if (pieza == null) {
            throw new IllegalArgumentException(
                    "La pieza no puede ser nula."
            );
        }

        validarCampo(pieza.getCodigo(), "El código", 20);
        validarCampo(pieza.getNombre(), "El nombre", 100);

        // La descripción es opcional, pero tiene una longitud máxima
        if (pieza.getDescripcion() != null &&
                pieza.getDescripcion().length() > 500) {

            throw new IllegalArgumentException(
                    "La descripción no puede superar los 500 caracteres."
            );
        }

        // El precio debe ser obligatorio y positivo
        BigDecimal precio = pieza.getPrecio();

        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser mayor que cero."
            );
        }
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

    // Comprueba que un texto de búsqueda no esté vacío
    private void validarTexto(String valor, String campo) {

        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    campo + " no puede estar vacío."
            );
        }
    }
}
