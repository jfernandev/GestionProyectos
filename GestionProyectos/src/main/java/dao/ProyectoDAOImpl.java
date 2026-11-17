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

            transaction = session.beginTransaction();

            session.persist(proyecto);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Error al guardar el proyecto.", e);
        }
    }

    @Override
    public Proyecto buscarPorCodigo(String codigo) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.find(Proyecto.class, codigo);

        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el proyecto.", e);
        }
    }

    @Override
    public List<Proyecto> buscarPorNombre(String nombre) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

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

            transaction = session.beginTransaction();

            session.merge(proyecto);

            transaction.commit();

        } catch (Exception e) {

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

            transaction = session.beginTransaction();

            Proyecto proyecto = session.find(Proyecto.class, codigo);

            if (proyecto == null) {
                throw new RuntimeException("No existe ningún proyecto con ese código.");
            }

            session.remove(proyecto);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("Error al eliminar el proyecto. Comprueba que no tenga gestiones asociadas.", e);
        }
    }
}
