
package utilidades;

import modelo.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SesionUsuario {

    // Logger para registrar la actividad de las sesiones
    private static final Logger logger =
            LoggerFactory.getLogger(SesionUsuario.class);

    // Usuario que tiene la sesión iniciada actualmente
    private static Usuario usuarioActual;

    // Evitamos crear instancias de esta clase
    private SesionUsuario() {
    }

    // Guarda el usuario autenticado
    public static void establecerUsuario(Usuario usuario) {

        if (usuario == null) {
            throw new IllegalArgumentException(
                    "El usuario no puede ser nulo."
            );
        }

        if (haySesionIniciada()) {
            throw new IllegalStateException(
                    "Ya existe una sesión iniciada."
            );
        }

        usuarioActual = usuario;

        logger.info(
                "Sesión establecida para el usuario: {}",
                usuario.getNombreUsuario()
        );
    }

    // Devuelve el usuario de la sesión actual
    public static Usuario obtenerUsuarioActual() {
        return usuarioActual;
    }

    // Comprueba si hay algún usuario autenticado
    public static boolean haySesionIniciada() {
        return usuarioActual != null;
    }

    // Comprueba si el usuario actual es administrador
    public static boolean esAdministrador() {

        return usuarioActual != null
                && "ADMIN".equals(usuarioActual.getRol());
    }

    // Impide ejecutar operaciones si no hay un administrador autenticado
    public static void exigirAdministrador() {

        if (!esAdministrador()) {
            logger.warn(
                    "Operación administrativa rechazada. Usuario: {}",
                    usuarioActual == null
                            ? "Sin sesión"
                            : usuarioActual.getNombreUsuario()
            );

            throw new SecurityException(
                    "No tienes permisos para realizar esta operación."
            );
        }
    }

    // Cierra la sesión actual
    public static void cerrarSesion() {

        if (usuarioActual != null) {

            logger.info(
                    "Sesión cerrada para el usuario: {}",
                    usuarioActual.getNombreUsuario()
            );

            usuarioActual = null;
        }
    }
}
