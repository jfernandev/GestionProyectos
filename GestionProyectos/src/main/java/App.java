
import menu.MenuAcceso;
import menu.MenuPrincipal;
import modelo.Usuario;
import servicios.UsuarioServicio;
import utilidades.HibernateUtil;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {

            // Creamos el servicio de usuarios
            UsuarioServicio usuarioServicio = new UsuarioServicio();

            // Mostramos el menú de acceso
            MenuAcceso menuAcceso = new MenuAcceso(
                    scanner,
                    usuarioServicio
            );

            Usuario usuario = menuAcceso.mostrarMenu();

            // Solo entramos en la aplicación si el acceso es correcto
            if (usuario != null) {

                System.out.println(
                        "\nSesión iniciada como: " +
                                usuario.getNombreUsuario()
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

            System.err.println(
                    "Error al iniciar la aplicación: " + e.getMessage()
            );

        } finally {

            // Cerramos Hibernate y liberamos el Scanner
            HibernateUtil.cerrar();
            scanner.close();
        }
    }
}
