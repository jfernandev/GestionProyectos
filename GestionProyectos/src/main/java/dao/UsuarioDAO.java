package dao;

import modelo.Usuario;

public interface UsuarioDAO {

    void guardar(Usuario usuario);

    Usuario buscarPorNombreUsuario(String nombreUsuario);

    boolean existeNombreUsuario(String nombreUsuario);

    boolean hayUsuariosRegistrados();
}
