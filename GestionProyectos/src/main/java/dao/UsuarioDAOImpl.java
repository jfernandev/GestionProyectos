
package dao;

import modelo.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utilidades.HibernateUtil;

public class UsuarioDAOImpl implements UsuarioDAO {

    @Override
    public void guardar(Usuario usuario) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(usuario);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException(
                    "Error al guardar el usuario.", e
            );
        }
    }

    @Override
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery(
                            "FROM Usuario u WHERE u.nombreUsuario = :nombre",
                            Usuario.class
                    )
                    .setParameter("nombre", nombreUsuario)
                    .uniqueResult();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error al buscar el usuario.", e
            );
        }
    }

    @Override
    public boolean existeNombreUsuario(String nombreUsuario) {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            Long cantidad = session.createQuery(
                            "SELECT COUNT(u) FROM Usuario u " +
                                    "WHERE u.nombreUsuario = :nombre",
                            Long.class
                    )
                    .setParameter("nombre", nombreUsuario)
                    .getSingleResult();

            return cantidad > 0;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Error al comprobar el nombre de usuario.", e
            );
        }
    }
}
