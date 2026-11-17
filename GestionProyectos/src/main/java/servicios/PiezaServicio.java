package servicios;

import dao.PiezaDAO;
import dao.PiezaDAOImpl;
import modelo.Pieza;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class PiezaServicio {

    // Logger encargado de registrar las operaciones y los errores
    private static final Logger logger =
            LoggerFactory.getLogger(PiezaServicio.class);

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

        try {
            validarPieza(pieza);

            // Comprobamos que no exista otra pieza con el mismo código
            if (piezaDAO.buscarPorCodigo(pieza.getCodigo()) != null) {
                throw new IllegalArgumentException(
                        "Ya existe una pieza con ese código."
                );
            }

            piezaDAO.guardar(pieza);

            logger.info("Pieza registrada correctamente. Código: {}",
                    pieza.getCodigo());

        } catch (RuntimeException e) {
            logger.error("Error al registrar la pieza.", e);
            throw e;
        }
    }

    // Busca una pieza por su código
    public Pieza buscarPorCodigo(String codigo) {

        try {
            validarTexto(codigo, "El código");

            Pieza pieza = piezaDAO.buscarPorCodigo(codigo.trim());

            logger.info("Búsqueda de pieza por código: {}", codigo.trim());

            return pieza;

        } catch (RuntimeException e) {
            logger.error("Error al buscar la pieza por código: {}", codigo, e);
            throw e;
        }
    }

    // Busca piezas por su nombre
    public List<Pieza> buscarPorNombre(String nombre) {

        try {
            validarTexto(nombre, "El nombre");

            List<Pieza> piezas = piezaDAO.buscarPorNombre(nombre.trim());

            logger.info("Búsqueda de piezas por nombre realizada. Resultados: {}",
                    piezas.size());

            return piezas;

        } catch (RuntimeException e) {
            logger.error("Error al buscar piezas por nombre.", e);
            throw e;
        }
    }

    // Devuelve todas las piezas registradas
    public List<Pieza> listarTodas() {

        try {
            List<Pieza> piezas = piezaDAO.listarTodas();

            logger.info("Listado de piezas realizado. Total: {}",
                    piezas.size());

            return piezas;

        } catch (RuntimeException e) {
            logger.error("Error al listar las piezas.", e);
            throw e;
        }
    }

    // Actualiza una pieza existente
    public void actualizar(Pieza pieza) {

        try {
            validarPieza(pieza);

            // Comprobamos que la pieza exista
            if (piezaDAO.buscarPorCodigo(pieza.getCodigo()) == null) {
                throw new IllegalArgumentException(
                        "No existe ninguna pieza con ese código."
                );
            }

            piezaDAO.actualizar(pieza);

            logger.info("Pieza actualizada correctamente. Código: {}",
                    pieza.getCodigo());

        } catch (RuntimeException e) {
            logger.error("Error al actualizar la pieza.", e);
            throw e;
        }
    }

    // Elimina una pieza por su código
    public void eliminar(String codigo) {

        try {
            validarTexto(codigo, "El código");

            // Comprobamos que la pieza exista
            if (piezaDAO.buscarPorCodigo(codigo.trim()) == null) {
                throw new IllegalArgumentException(
                        "No existe ninguna pieza con ese código."
                );
            }

            piezaDAO.eliminar(codigo.trim());

            logger.info("Pieza eliminada correctamente. Código: {}",
                    codigo.trim());

        } catch (RuntimeException e) {
            logger.error("Error al eliminar la pieza. Código: {}", codigo, e);
            throw e;
        }
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