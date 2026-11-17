
package dao;

import modelo.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilidades.HibernateUtil;

public class UsuarioDAOImpl implements UsuarioDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(UsuarioDAOImpl.class);

    @Override
    public void guardar(Usuario usuario) {

        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(usuario);

            transaction.commit();

            logger.info(
                    "Usuario guardado correctamente: {}",
                    usuario.getNombreUsuario()
            );

        } catch (Exception e) {

            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            logger.error(
                    "Error al guardar el usuario.",
                    e
            );

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

            logger.error(
                    "Error al buscar el usuario: {}",
                    nombreUsuario,
                    e
            );

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

            logger.error(
                    "Error al comprobar el nombre de usuario: {}",
                    nombreUsuario,
                    e
            );

            throw new RuntimeException(
                    "Error al comprobar el nombre de usuario.", e
            );
        }
    }

    @Override
    public boolean hayUsuariosRegistrados() {

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            Long cantidad = session.createQuery(
                    "SELECT COUNT(u) FROM Usuario u",
                    Long.class
            ).getSingleResult();

            return cantidad > 0;

        } catch (Exception e) {

            logger.error(
                    "Error al comprobar los usuarios registrados.",
                    e
            );

            throw new RuntimeException(
                    "Error al comprobar los usuarios registrados.", e
            );
        }
    }
}
