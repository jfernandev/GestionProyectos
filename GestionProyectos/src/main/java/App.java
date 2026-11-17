
import menu.MenuAcceso;
import menu.MenuPrincipal;
import modelo.Usuario;
import servicios.UsuarioServicio;
import utilidades.HibernateUtil;
import utilidades.SesionUsuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class App {

    // Logger para registrar el inicio y cierre de la aplicación
    private static final Logger logger =
            LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {
            // Creamos el servicio de autenticación
            UsuarioServicio usuarioServicio = new UsuarioServicio();

            // Mostramos el menú de acceso
            MenuAcceso menuAcceso = new MenuAcceso(
                    scanner,
                    usuarioServicio
            );

            Usuario usuario = menuAcceso.mostrarMenu();

            if (usuario != null) {

                // Guardamos el usuario autenticado en la sesión
                SesionUsuario.establecerUsuario(usuario);

                logger.info(
                        "Aplicación iniciada por el usuario: {}",
                        usuario.getNombreUsuario()
                );

                System.out.println(
                        "\nSesión iniciada como: "
                                + usuario.getNombreUsuario()
                );

                // Abrimos el menú principal
                MenuPrincipal menuPrincipal =
                        new MenuPrincipal(scanner, usuario);

                menuPrincipal.iniciar();

            } else {
                System.out.println(
                        "\nGracias por utilizar GestionProyectos."
                );
            }

        } catch (Exception e) {

            logger.error("Se ha producido un error en la aplicación.", e);

            System.err.println(
                    "Error al iniciar la aplicación: "
                            + e.getMessage()
            );

        } finally {

            // Cerramos la sesión
            SesionUsuario.cerrarSesion();

            // Cerramos Hibernate y el Scanner
            HibernateUtil.cerrar();
            scanner.close();

            logger.info("Aplicación finalizada.");
        }
    }
}
