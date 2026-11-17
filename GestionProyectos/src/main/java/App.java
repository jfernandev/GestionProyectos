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

    private static final Logger logger =
            LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {
            UsuarioServicio usuarioServicio = new UsuarioServicio();

            MenuAcceso menuAcceso = new MenuAcceso(
                    scanner,
                    usuarioServicio
            );

            Usuario usuario = menuAcceso.mostrarMenu();

            if (usuario != null) {

                SesionUsuario.establecerUsuario(usuario);

                logger.info(
                        "Aplicación iniciada por el usuario: {}",
                        usuario.getNombreUsuario()
                );

                System.out.println(
                        "\nSesión iniciada como: "
                                + usuario.getNombreUsuario()
                );

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

            SesionUsuario.cerrarSesion();

            HibernateUtil.cerrar();
            scanner.close();

            logger.info("Aplicación finalizada.");
        }
    }
}
