package menu;

import modelo.Usuario;
import servicios.GestionServicio;
import utilidades.Entrada;

import java.util.Scanner;

public class MenuPrincipal {

    private final Scanner scanner;
    private final Usuario usuario;

    public MenuPrincipal(Scanner scanner, Usuario usuario) {
        this.scanner = scanner;
        this.usuario = usuario;
    }

    public void iniciar() {

        int opcion;

        do {
            mostrarMenu();

            opcion = Entrada.leerEntero(
                    scanner,
                    "Selecciona una opción: "
            );

            if (esAdministrador()) {
                procesarOpcionAdministrador(opcion);
            } else {
                procesarOpcionUsuario(opcion);
            }

        } while (esAdministrador() ? opcion != 6 : opcion != 2);
    }

    private void procesarOpcionAdministrador(int opcion) {

        switch (opcion) {

            case 1:
                new MenuProveedores(scanner).iniciar();
                break;

            case 2:
                new MenuPiezas(scanner).iniciar();
                break;

            case 3:
                new MenuProyectos(scanner).iniciar();
                break;

            case 4:
                new MenuGestiones(scanner).iniciar();
                break;

            case 5:
                mostrarEstadisticas();
                break;

            case 6:
                System.out.println("\nCerrando la aplicación...");
                break;

            default:
                System.out.println("\nOpción no válida.");
                break;
        }
    }

    private void procesarOpcionUsuario(int opcion) {

        switch (opcion) {

            case 1:
                mostrarEstadisticas();
                break;

            case 2:
                System.out.println("\nCerrando la aplicación...");
                break;

            default:
                System.out.println("\nOpción no válida.");
                break;
        }
    }

    private void mostrarEstadisticas() {

        MenuEstadisticas menuEstadisticas =
                new MenuEstadisticas(
                        scanner,
                        new GestionServicio()
                );

        menuEstadisticas.mostrarMenu();
    }

    private boolean esAdministrador() {
        return "ADMIN".equals(usuario.getRol());
    }

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
