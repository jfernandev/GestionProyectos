package utilidades;

import modelo.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SesionUsuario {

    private static final Logger logger =
            LoggerFactory.getLogger(SesionUsuario.class);

    private static Usuario usuarioActual;

    private SesionUsuario() {
    }

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

    public static Usuario obtenerUsuarioActual() {
        return usuarioActual;
    }

    public static boolean haySesionIniciada() {
        return usuarioActual != null;
    }

    public static boolean esAdministrador() {

        return usuarioActual != null
                && "ADMIN".equals(usuarioActual.getRol());
    }

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
