
package dao;

import modelo.Usuario;

public interface UsuarioDAO {

    // Guarda un usuario en la base de datos
    void guardar(Usuario usuario);

    // Busca un usuario por su nombre de usuario
    Usuario buscarPorNombreUsuario(String nombreUsuario);

    // Comprueba si un nombre de usuario ya está registrado
    boolean existeNombreUsuario(String nombreUsuario);

    // Comprueba si ya existe algún usuario registrado
    boolean hayUsuariosRegistrados();
}
