
package dao;

import modelo.Pieza;

import java.util.List;

public interface PiezaDAO {

    // Guarda una pieza nueva en la base de datos
    void guardar(Pieza pieza);

    // Busca una pieza por su código
    Pieza buscarPorCodigo(String codigo);

    // Busca piezas cuyo nombre contenga el texto indicado
    List<Pieza> buscarPorNombre(String nombre);

    // Devuelve todas las piezas registradas
    List<Pieza> listarTodas();

    // Actualiza los datos de una pieza existente
    void actualizar(Pieza pieza);

    // Elimina una pieza a partir de su código
    void eliminar(String codigo);
}
