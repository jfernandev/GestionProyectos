
package menu;

import utilidades.Entrada;

import java.util.Scanner;

public class MenuPrincipal {

    // Scanner compartido por todos los menús de la aplicación
    private final Scanner scanner;

    // Constructor: recibe el Scanner creado desde App
    public MenuPrincipal(Scanner scanner) {
        this.scanner = scanner;
    }

    // Inicia el menú principal de la aplicación
    public void iniciar() {

        int opcion;

        do {
            mostrarMenu();

            opcion = Entrada.leerEntero(scanner, "Selecciona una opción: ");

            switch (opcion) {

                case 1:
                    System.out.println("\nApartado de gestión de proveedores.");
                    // Aquí conectaremos posteriormente el menú de proveedores
                    break;

                case 2:
                    System.out.println("\nApartado de gestión de piezas.");
                    // Aquí conectaremos posteriormente el menú de piezas
                    break;

                case 3:
                    System.out.println("\nApartado de gestión de proyectos.");
                    // Aquí conectaremos posteriormente el menú de proyectos
                    break;

                case 4:
                    System.out.println("\nApartado de gestión de gestiones.");
                    // Aquí conectaremos posteriormente el menú de gestiones
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

    // Muestra las opciones disponibles
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
