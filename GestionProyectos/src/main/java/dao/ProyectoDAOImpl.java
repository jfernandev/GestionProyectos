
package dao;

import modelo.Proyecto;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utilidades.HibernateUtil;

import java.util.List;

public class ProyectoDAOImpl implements ProyectoDAO {

    @Override
    public void guardar(Proyecto proyecto) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Iniciamos la transacción
            transaction = session.beginTransaction();

            // Guardamos el proyecto
            session.persist(proyecto);

            // Confirmamos los cambios
            transaction.commit();

        } catch (Exception e) {

            // Deshacemos los cambios si ocurre algún error
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Error al guardar el proyecto.", e);
        }
    }

    @Override
    public Proyecto buscarPorCodigo(String codigo) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Buscamos el proyecto mediante su clave primaria
            return session.find(Proyecto.class, codigo);

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el proyecto.", e);
        }
    }

    @Override
    public List<Proyecto> buscarPorNombre(String nombre) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Buscamos proyectos cuyo nombre contenga el texto indicado
            return session.createQuery(
                            "FROM Proyecto p WHERE LOWER(p.nombre) LIKE LOWER(:nombre)",
                            Proyecto.class
                    )
                    .setParameter("nombre", "%" + nombre + "%")
                    .getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar proyectos por nombre.", e);
        }
    }

    @Override
    public List<Proyecto> buscarPorCiudad(String ciudad) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Buscamos proyectos cuya ciudad contenga el texto indicado
            return session.createQuery(
                            "FROM Proyecto p WHERE LOWER(p.ciudad) LIKE LOWER(:ciudad)",
                            Proyecto.class
                    )
                    .setParameter("ciudad", "%" + ciudad + "%")
                    .getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar proyectos por ciudad.", e);
        }
    }

    @Override
    public List<Proyecto> listarTodos() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Recuperamos todos los proyectos ordenados por código
            return session.createQuery(
                    "FROM Proyecto p ORDER BY p.codigo",
                    Proyecto.class
            ).getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Error al listar los proyectos.", e);
        }
    }

    @Override
    public void actualizar(Proyecto proyecto) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Iniciamos la transacción
            transaction = session.beginTransaction();

            // Actualizamos el proyecto
            session.merge(proyecto);

            // Confirmamos los cambios
            transaction.commit();

        } catch (Exception e) {

            // Deshacemos los cambios si ocurre algún error
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Error al actualizar el proyecto.", e);
        }
    }

    @Override
    public void eliminar(String codigo) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            // Iniciamos la transacción
            transaction = session.beginTransaction();

            // Buscamos el proyecto que queremos eliminar
            Proyecto proyecto = session.find(Proyecto.class, codigo);

            if (proyecto == null) {
                throw new RuntimeException("No existe ningún proyecto con ese código.");
            }

            // Eliminamos el proyecto
            session.remove(proyecto);

            // Confirmamos los cambios
            transaction.commit();

        } catch (Exception e) {

            // Deshacemos los cambios si ocurre algún error
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Error al eliminar el proyecto. Comprueba que no tenga gestiones asociadas.", e);
        }
    }
}
