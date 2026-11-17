
package utilidades;

import modelo.Usuario;

public class SesionUsuario {

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

        usuarioActual = usuario;
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
            throw new SecurityException(
                    "No tienes permisos para realizar esta operación."
            );
        }
    }

    // Cierra la sesión actual
    public static void cerrarSesion() {
        usuarioActual = null;
    }
}
