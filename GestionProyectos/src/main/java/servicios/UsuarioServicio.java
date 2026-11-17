
package servicios;

import dao.UsuarioDAO;
import dao.UsuarioDAOImpl;
import modelo.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsuarioServicio {

    // Logger para registrar los acontecimientos relacionados con la seguridad
    private static final Logger logger =
            LoggerFactory.getLogger(UsuarioServicio.class);

    private final UsuarioDAO usuarioDAO;

    // Constructor para utilizar el DAO real
    public UsuarioServicio() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    // Constructor alternativo para las pruebas con Mockito
    public UsuarioServicio(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    // Crea el primer administrador de la aplicación
    public void crearAdministradorInicial(
            String nombreUsuario,
            String contrasena
    ) {

        if (usuarioDAO.hayUsuariosRegistrados()) {
            logger.warn("Intento de crear un administrador inicial cuando ya existen usuarios.");

            throw new IllegalStateException(
                    "Ya existe un usuario registrado. " +
                            "No se puede crear otro administrador inicial."
            );
        }

        registrar(nombreUsuario, contrasena, "ADMIN");

        logger.info("Se ha creado el administrador inicial: {}", nombreUsuario);
    }

    // Registra un usuario normal
    public void registrarUsuario(
            String nombreUsuario,
            String contrasena
    ) {

        registrar(nombreUsuario, contrasena, "USER");

        logger.info("Se ha registrado un nuevo usuario: {}", nombreUsuario);
    }

    // Centraliza las validaciones y el registro de usuarios
    private void registrar(
            String nombreUsuario,
            String contrasena,
            String rol
    ) {

        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de usuario es obligatorio."
            );
        }

        if (contrasena == null || contrasena.length() < 8) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 8 caracteres."
            );
        }

        if (usuarioDAO.existeNombreUsuario(nombreUsuario)) {
            throw new IllegalArgumentException(
                    "Ese nombre de usuario ya está registrado."
            );
        }

        // Generamos el hash BCrypt de la contraseña
        String contrasenaHash = BCrypt.hashpw(
                contrasena,
                BCrypt.gensalt(12)
        );

        Usuario usuario = new Usuario(
                nombreUsuario,
                contrasenaHash,
                rol
        );

        usuarioDAO.guardar(usuario);
    }

    // Comprueba las credenciales de acceso
    public Usuario iniciarSesion(
            String nombreUsuario,
            String contrasena
    ) {

        if (nombreUsuario == null || nombreUsuario.isBlank()
                || contrasena == null || contrasena.isBlank()) {

            logger.warn("Intento de inicio de sesión con datos incompletos.");

            throw new IllegalArgumentException(
                    "Debes introducir el usuario y la contraseña."
            );
        }

        Usuario usuario =
                usuarioDAO.buscarPorNombreUsuario(nombreUsuario);

        if (usuario == null) {
            logger.warn(
                    "Intento de acceso fallido. Usuario inexistente: {}",
                    nombreUsuario
            );

            throw new IllegalArgumentException(
                    "Usuario o contraseña incorrectos."
            );
        }

        if (!usuario.isActivo()) {
            logger.warn(
                    "Intento de acceso con una cuenta desactivada: {}",
                    nombreUsuario
            );

            throw new IllegalArgumentException(
                    "Esta cuenta está desactivada."
            );
        }

        // Comprobamos la contraseña contra el hash almacenado
        if (!BCrypt.checkpw(
                contrasena,
                usuario.getContrasenaHash()
        )) {

            logger.warn(
                    "Intento de acceso con contraseña incorrecta: {}",
                    nombreUsuario
            );

            throw new IllegalArgumentException(
                    "Usuario o contraseña incorrectos."
            );
        }

        logger.info(
                "Inicio de sesión correcto. Usuario: {} | Rol: {}",
                usuario.getNombreUsuario(),
                usuario.getRol()
        );

        return usuario;
    }
}
