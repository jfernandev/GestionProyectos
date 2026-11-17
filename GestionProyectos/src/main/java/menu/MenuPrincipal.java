
package menu;

import modelo.Usuario;
import utilidades.Entrada;
import servicios.GestionServicio;

import java.util.Scanner;

public class MenuPrincipal {

    private final Scanner scanner;
    private final Usuario usuario;

    // Constructor: recibe el Scanner y el usuario autenticado
    public MenuPrincipal(Scanner scanner, Usuario usuario) {
        this.scanner = scanner;
        this.usuario = usuario;
    }

    // Inicia el menú principal
    public void iniciar() {

        int opcion;

        do {
            mostrarMenu();

            opcion = Entrada.leerEntero(
                    scanner,
                    "Selecciona una opción: "
            );

            switch (opcion) {

                case 1:
                    if (esAdministrador()) {
                        MenuProveedores menuProveedores =
                                new MenuProveedores(scanner);
                        menuProveedores.iniciar();
                    } else {
                        mostrarAccesoDenegado();
                    }
                    break;

                case 2:
                    if (esAdministrador()) {
                        MenuPiezas menuPiezas =
                                new MenuPiezas(scanner);
                        menuPiezas.iniciar();
                    } else {
                        mostrarAccesoDenegado();
                    }
                    break;

                case 3:
                    if (esAdministrador()) {
                        MenuProyectos menuProyectos =
                                new MenuProyectos(scanner);
                        menuProyectos.iniciar();
                    } else {
                        mostrarAccesoDenegado();
                    }
                    break;

                case 4:
                    if (esAdministrador()) {
                        MenuGestiones menuGestiones =
                                new MenuGestiones(scanner);
                        menuGestiones.iniciar();
                    } else {
                        mostrarAccesoDenegado();
                    }
                    break;

                case 5:
                    // Las estadísticas están disponibles para ambos roles
                    MenuEstadisticas menuEstadisticas =
                            new MenuEstadisticas(
                                    scanner,
                                    new GestionServicio()
                            );

                    menuEstadisticas.mostrarMenu();
                    break;

                case 6:
                    if (esAdministrador()) {
                        System.out.println(
                                "\nCerrando la aplicación..."
                        );
                    } else {
                        mostrarAccesoDenegado();
                    }
                    break;

                case 2:
                    if (!esAdministrador()) {
                        System.out.println(
                                "\nCerrando la aplicación..."
                        );
                    }
                    break;

                default:
                    System.out.println("\nOpción no válida.");
                    break;
            }

        } while (esAdministrador() ? opcion != 6 : opcion != 2);
    }

    // Comprueba si el usuario tiene permisos de administrador
    private boolean esAdministrador() {
        return "ADMIN".equals(usuario.getRol());
    }

    // Muestra un mensaje cuando el usuario no tiene permisos
    private void mostrarAccesoDenegado() {
        System.out.println(
                "\nNo tienes permisos para realizar esta operación."
        );
    }

    // Muestra las opciones disponibles según el rol
    public void mostrarMenu() {

        System.out.println("\n========================================");
        System.out.println("       GESTIÓN DE PROYECTOS");
        System.out.println("========================================");
        System.out.println(
                "Usuario: " + usuario.getNombreUsuario()
        );
        System.out.println(
                "Rol: " + usuario.getRol()
        );
        System.out.println("----------------------------------------");

        if (esAdministrador()) {

            System.out.println("1. Gestión de proveedores");
            System.out.println("2. Gestión de piezas");
            System.out.println("3. Gestión de proyectos");
            System.out.println("4. Gestión de gestiones");
            System.out.println("5. Estadísticas");
            System.out.println("6. Salir");

        } else {

            System.out.println("1. Estadísticas");
            System.out.println("2. Salir");
        }

        System.out.println("========================================");
    }
}
