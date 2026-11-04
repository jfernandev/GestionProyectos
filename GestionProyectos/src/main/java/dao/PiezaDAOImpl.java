
package dao;

import modelo.Pieza;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utilidades.HibernateUtil;

import java.util.List;

public class PiezaDAOImpl implements PiezaDAO {

    @Override
    public void guardar(Pieza pieza) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Iniciamos la transacción
            transaction = session.beginTransaction();

            // Guardamos la pieza
            session.persist(pieza);

            // Confirmamos los cambios
            transaction.commit();

        } catch (Exception e) {

            // Deshacemos los cambios si ocurre algún error
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Error al guardar la pieza.", e);
        }
    }

    @Override
    public Pieza buscarPorCodigo(String codigo) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Buscamos la pieza mediante su clave primaria
            return session.find(Pieza.class, codigo);

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la pieza.", e);
        }
    }

    @Override
    public List<Pieza> buscarPorNombre(String nombre) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Buscamos piezas cuyo nombre contenga el texto indicado
            return session.createQuery(
                            "FROM Pieza p WHERE LOWER(p.nombre) LIKE LOWER(:nombre)",
                            Pieza.class
                    )
                    .setParameter("nombre", "%" + nombre + "%")
                    .getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar piezas por nombre.", e);
        }
    }

    @Override
    public List<Pieza> listarTodas() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Recuperamos todas las piezas ordenadas por código
            return session.createQuery(
                    "FROM Pieza p ORDER BY p.codigo",
                    Pieza.class
            ).getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error al listar las piezas.", e);
        }
    }

    @Override
    public void actualizar(Pieza pieza) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Iniciamos la transacción
            transaction = session.beginTransaction();

            // Actualizamos la pieza
            session.merge(pieza);

            // Confirmamos los cambios
            transaction.commit();

        } catch (Exception e) {

            // Deshacemos los cambios si ocurre algún error
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Error al actualizar la pieza.", e);
        }
    }

    @Override
    public void eliminar(String codigo) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Iniciamos la transacción
            transaction = session.beginTransaction();

            // Buscamos la pieza que queremos eliminar
            Pieza pieza = session.find(Pieza.class, codigo);

            if (pieza == null) {
                throw new RuntimeException("No existe ninguna pieza con ese código.");
            }

            // Eliminamos la pieza
            session.remove(pieza);

            // Confirmamos los cambios
            transaction.commit();

        } catch (Exception e) {

            // Deshacemos los cambios si ocurre algún error
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Error al eliminar la pieza. Comprueba que no tenga gestiones asociadas.", e);
        }
    }
}
