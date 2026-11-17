
package menu;

import utilidades.Entrada;

import java.util.Scanner;

public class MenuPrincipal {

    private final Scanner scanner;

    // Constructor: recibe el Scanner compartido por la aplicación
    public MenuPrincipal(Scanner scanner) {
        this.scanner = scanner;
    }

    // Inicia el menú principal
    public void iniciar() {

        int opcion;

        do {
            mostrarMenu();

            opcion = Entrada.leerEntero(scanner, "Selecciona una opción: ");

            switch (opcion) {

                case 1:
                    MenuProveedores menuProveedores = new MenuProveedores(scanner);
                    menuProveedores.iniciar();
                    break;

                case 2:
                    MenuPiezas menuPiezas = new MenuPiezas(scanner);
                    menuPiezas.iniciar();
                    break;

                case 3:
                    MenuProyectos menuProyectos = new MenuProyectos(scanner);
                    menuProyectos.iniciar();
                    break;

                case 4:
                    MenuGestiones menuGestiones = new MenuGestiones(scanner);
                    menuGestiones.iniciar();
                    break;

                case 5:
                    System.out.println("\nCerrando la aplicación...");
                    break;

                default:
                    System.out.println("\nError: selecciona una opción entre 1 y 5.");
                    break;
            }

        } while (opcion != 5);
    }

    // Muestra las opciones principales
    private void mostrarMenu() {

        System.out.println("\n========================================");
        System.out.println("       GESTIÓN DE PROYECTOS");
        System.out.println("========================================");
        System.out.println("1. Gestión de proveedores");
        System.out.println("2. Gestión de piezas");
        System.out.println("3. Gestión de proyectos");
        System.out.println("4. Gestión de gestiones");
        System.out.println("5. Salir");
        System.out.println("========================================");
    }
}
