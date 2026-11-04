
import menu.MenuPrincipal;
import utilidades.HibernateUtil;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        // Scanner compartido por todos los menús
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Iniciando aplicación...");

            // Inicializamos Hibernate y comprobamos la conexión
            HibernateUtil.getSessionFactory();

            System.out.println("Hibernate inicializado correctamente.");

            // Creamos e iniciamos el menú principal
            MenuPrincipal menuPrincipal = new MenuPrincipal(scanner);
            menuPrincipal.iniciar();

        } catch (Exception e) {
            System.err.println("Error durante la ejecución de la aplicación:");
            e.printStackTrace();

        } finally {
            // Cerramos Hibernate al finalizar la aplicación
            HibernateUtil.cerrar();

            // Cerramos el Scanner
            scanner.close();
        }
    }
}
