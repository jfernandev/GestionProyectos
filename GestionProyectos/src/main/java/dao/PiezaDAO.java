package dao;

import modelo.Pieza;

import java.util.List;

public interface PiezaDAO {

    void guardar(Pieza pieza);

    Pieza buscarPorCodigo(String codigo);

    List<Pieza> buscarPorNombre(String nombre);

    List<Pieza> listarTodas();

    void actualizar(Pieza pieza);

    void eliminar(String codigo);
}
