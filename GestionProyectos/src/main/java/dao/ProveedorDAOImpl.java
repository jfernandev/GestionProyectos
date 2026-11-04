
package dao;

import modelo.Proveedor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utilidades.HibernateUtil;

import java.util.List;

public class ProveedorDAOImpl implements ProveedorDAO {

    @Override
    public void guardar(Proveedor proveedor) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Iniciamos la transacción
            transaction = session.beginTransaction();

            // Guardamos el proveedor
            session.persist(proveedor);

            // Confirmamos los cambios
            transaction.commit();

        } catch (Exception e) {

            // Si ocurre un error, deshacemos los cambios
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Error al guardar el proveedor.", e);
        }
    }

    @Override
    public Proveedor buscarPorCodigo(String codigo) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Buscamos el proveedor utilizando su clave primaria
            return session.find(Proveedor.class, codigo);

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el proveedor.", e);
        }
    }

    @Override
    public List<Proveedor> buscarPorNombre(String nombre) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Buscamos coincidencias parciales en el nombre
            return session.createQuery(
                            "FROM Proveedor p WHERE LOWER(p.nombre) LIKE LOWER(:nombre)",
                            Proveedor.class
                    )
                    .setParameter("nombre", "%" + nombre + "%")
                    .getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar proveedores por nombre.", e);
        }
    }

    @Override
    public List<Proveedor> buscarPorDireccion(String direccion) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Buscamos coincidencias parciales en la dirección
            return session.createQuery(
                            "FROM Proveedor p WHERE LOWER(p.direccion) LIKE LOWER(:direccion)",
                            Proveedor.class
                    )
                    .setParameter("direccion", "%" + direccion + "%")
                    .getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar proveedores por dirección.", e);
        }
    }

    @Override
    public List<Proveedor> listarTodos() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Recuperamos todos los proveedores ordenados por código
            return session.createQuery(
                    "FROM Proveedor p ORDER BY p.codigo",
                    Proveedor.class
            ).getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error al listar los proveedores.", e);
        }
    }

    @Override
    public void actualizar(Proveedor proveedor) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Iniciamos la transacción
            transaction = session.beginTransaction();

            // Actualizamos el proveedor
            session.merge(proveedor);

            // Confirmamos los cambios
            transaction.commit();

        } catch (Exception e) {

            // Deshacemos los cambios si ocurre algún error
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Error al actualizar el proveedor.", e);
        }
    }

    @Override
    public void eliminar(String codigo) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Iniciamos la transacción
            transaction = session.beginTransaction();

            // Buscamos el proveedor que queremos eliminar
            Proveedor proveedor = session.find(Proveedor.class, codigo);

            if (proveedor == null) {
                throw new RuntimeException("No existe ningún proveedor con ese código.");
            }

            // Eliminamos el proveedor
            session.remove(proveedor);

            // Confirmamos los cambios
            transaction.commit();

        } catch (Exception e) {

            // Deshacemos los cambios si ocurre algún error
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Error al eliminar el proveedor. Comprueba que no tenga gestiones asociadas.", e);
        }
    }
}
