
package menu;

import modelo.Usuario;
import servicios.UsuarioServicio;

import java.util.Scanner;

public class MenuAcceso {

    private final Scanner scanner;
    private final UsuarioServicio usuarioServicio;

    // Constructor: recibe el Scanner y el servicio de usuarios
    public MenuAcceso(
            Scanner scanner,
            UsuarioServicio usuarioServicio
    ) {
        this.scanner = scanner;
        this.usuarioServicio = usuarioServicio;
    }

    // Muestra el menú de acceso y devuelve el usuario autenticado
    public Usuario mostrarMenu() {

        boolean salir = false;

        while (!salir) {

            System.out.println("\n===== ACCESO A GESTIONPROYECTOS =====");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Crear administrador inicial");
            System.out.println("3. Salir");
            System.out.print("Selecciona una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {

                case "1":
                    Usuario usuario = iniciarSesion();

                    if (usuario != null) {
                        return usuario;
                    }
                    break;

                case "2":
                    crearAdministradorInicial();
                    break;

                case "3":
                    salir = true;
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        }

        return null;
    }

    // Solicita las credenciales y comprueba el acceso
    private Usuario iniciarSesion() {

        System.out.println("\n===== INICIAR SESIÓN =====");

        System.out.print("Nombre de usuario: ");
        String nombreUsuario = scanner.nextLine();

        System.out.print("Contraseña: ");
        String contrasena = scanner.nextLine();

        try {

            Usuario usuario = usuarioServicio.iniciarSesion(
                    nombreUsuario,
                    contrasena
            );

            System.out.println(
                    "\nBienvenido, " + usuario.getNombreUsuario() + "."
            );

            return usuario;

        } catch (IllegalArgumentException e) {

            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    // Permite crear el primer administrador
    private void crearAdministradorInicial() {

        System.out.println("\n===== CREAR ADMINISTRADOR INICIAL =====");

        System.out.print("Nombre de usuario: ");
        String nombreUsuario = scanner.nextLine();

        System.out.print("Contraseña (mínimo 8 caracteres): ");
        String contrasena = scanner.nextLine();

        System.out.print("Repite la contraseña: ");
        String confirmacion = scanner.nextLine();

        if (!contrasena.equals(confirmacion)) {
            System.out.println("Las contraseñas no coinciden.");
            return;
        }

        try {

            usuarioServicio.crearAdministradorInicial(
                    nombreUsuario,
                    contrasena
            );

            System.out.println(
                    "Administrador creado correctamente."
            );

        } catch (IllegalArgumentException |
                 IllegalStateException e) {

            System.out.println("Error: " + e.getMessage());
        }
    }
}
